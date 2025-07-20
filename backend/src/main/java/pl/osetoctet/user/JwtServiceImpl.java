package pl.osetoctet.user;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import pl.osetoctet.common.exception.AuthenticationException;
import pl.osetoctet.user.model.entity.Token;
import pl.osetoctet.user.model.entity.User;
import pl.osetoctet.user.model.enums.TokenType;

import java.security.Key;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static pl.osetoctet.user.model.enums.TokenType.ACCESS_TOKEN;
import static pl.osetoctet.user.model.enums.TokenType.REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
class JwtServiceImpl implements JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.access-token.expiration}")
    private long jwtAccessTokenExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long jwtRefreshTokenExpiration;

    private final TokenRepository tokenRepository;
    private final UserEntityService userEntityService;

    @Override
    public UserDetails extractUser(String token) {
        try {
            String username = extractUsername(token);
            return userEntityService.findUserByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("auth.user.notFound"));
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new AuthenticationException("auth.token.invalid");
        }
    }

    @Override
    public String generateAccessToken(User user, String deviceId) {
        Date expirationDate = new Date(System.currentTimeMillis() + jwtAccessTokenExpiration);
        return buildToken(user.getUsername(), ACCESS_TOKEN, expirationDate, deviceId, user.getRole().name());
    }

    @Override
    public String generateRefreshToken(User user, String deviceId) {
        revokeAllUserTokensAssociatedWithDevice(user, deviceId);
        Date expirationDate = new Date(System.currentTimeMillis() + jwtRefreshTokenExpiration);
        String token = buildToken(user.getUsername(), REFRESH_TOKEN, expirationDate, deviceId, null);
        saveUserToken(user, token, expirationDate, deviceId);
        return token;
    }

    @Override
    public Optional<String> extractAndValidateAccessToken(HttpServletRequest request) {
        return extractAndValidateToken(request, this::isAccessToken);
    }

    @Override
    public boolean isRefreshTokenValid(String refreshToken, UserDetails userDetails) {
        return isRefreshToken(refreshToken) && isTokenValid(refreshToken, userDetails);
    }

    @Override
    public boolean isRefreshTokenValid(String refreshToken, UserDetails userDetails, String deviceId) {
        return isRefreshToken(refreshToken) && isTokenAssociatedWithDevice(refreshToken, deviceId) && isTokenValid(refreshToken, userDetails);
    }

    @Override
    public void authenticateUser(String jwt, HttpServletRequest request) {
        UserDetails userDetails = extractUser(jwt);
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            setAuthenticationContext(userDetails, request);
        }
    }

    @Override
    public void revokeAllUserTokens(User user) {
        tokenRepository.findAllValidTokenByUser(user.getId()).forEach(token -> {
            token.setRevoked(true);
            tokenRepository.save(token);
        });
    }

    @Override
    public void revokeToken(String token) {
        tokenRepository.findByToken(token).ifPresent(storedToken -> {
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
        });
    }

    private void setAuthenticationContext(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private boolean isTokenExpired(String token) {
        return tokenRepository.findByToken(token)
                .map(storedToken -> extractExpiration(token).before(new Date()) || storedToken.isRevoked())
                .orElseGet(() -> extractExpiration(token).before(new Date()));
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private void revokeAllUserTokensAssociatedWithDevice(User user, String deviceId) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUserAndDeviceId(user.getId(), deviceId);
        validUserTokens.forEach(token -> token.setRevoked(true));
        tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(User user, String jwtToken, Date expirationDate, String deviceId) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .expirationDate(expirationDate.toInstant().atOffset(ZoneOffset.UTC))
                .deviceId(deviceId)
                .build();
        tokenRepository.save(token);
    }

    private String buildToken(String username, TokenType type, Date expirationDate, String deviceId, String role) {
        return Jwts.builder()
                .setClaims(createClaims(type.getName(), deviceId, role))
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Map<String, Object> createClaims(String type, String deviceId, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", type);
        claims.put("jti", UUID.randomUUID().toString());
        claims.put("deviceId", deviceId);
        if (role != null) {
            claims.put("role", role);
        }
        return claims;
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Optional<String> extractAndValidateToken(HttpServletRequest request, Predicate<String> tokenTypeChecker) {
        try {
            return Optional.ofNullable(extractJwtFromHeader(request))
                    .filter(tokenTypeChecker)
                    .flatMap(jwt -> {
                        UserDetails userDetails = extractUser(jwt);
                        return isTokenValid(jwt, userDetails) ? Optional.of(jwt) : Optional.empty();
                    });
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private String extractJwtFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return (authHeader != null && authHeader.startsWith("Bearer")) ? authHeader.substring(7) : null;
    }

    private boolean isAccessToken(String token) {
        return ACCESS_TOKEN.getName().equals(extractClaim(token, claims -> claims.get("type")));
    }

    private boolean isRefreshToken(String token) {
        return REFRESH_TOKEN.getName().equals(extractClaim(token, claims -> claims.get("type")));
    }

    private boolean isTokenAssociatedWithDevice(String token, String deviceId) {
        return deviceId.equals(extractClaim(token, claims -> claims.get("deviceId")));
    }

    private boolean isTokenValid(String token, UserDetails userDetails) {
        return !isTokenExpired(token) && extractUsername(token).equals(userDetails.getUsername());
    }

}
