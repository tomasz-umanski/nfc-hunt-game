package pl.osetoctet.user;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import pl.osetoctet.user.model.entity.User;

import java.util.Optional;

public interface JwtService {

    UserDetails extractUser(String token);

    String generateAccessToken(User user, String deviceId);

    String generateRefreshToken(User user, String deviceId);

    Optional<String> extractAndValidateAccessToken(HttpServletRequest request);

    boolean isRefreshTokenValid(String refreshToken, UserDetails userDetails);

    boolean isRefreshTokenValid(String refreshToken, UserDetails userDetails, String deviceId);

    void authenticateUser(String jwt, HttpServletRequest request);

    void revokeAllUserTokens(User user);

    void revokeToken(String token);

}
