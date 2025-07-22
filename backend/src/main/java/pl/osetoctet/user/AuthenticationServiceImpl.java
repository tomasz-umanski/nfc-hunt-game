package pl.osetoctet.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.osetoctet.common.exception.SaveException;
import pl.osetoctet.common.exception.ValidationException;
import pl.osetoctet.user.model.dto.AuthenticateUserDto;
import pl.osetoctet.user.model.dto.AuthenticationResponse;
import pl.osetoctet.user.model.dto.RefreshTokenOperationsDto;
import pl.osetoctet.user.model.dto.RegisterUserDto;
import pl.osetoctet.user.model.entity.User;

@Slf4j
@Service
@RequiredArgsConstructor
class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtService jwtService;
    private final UserFactory userFactory;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthenticationResponse register(RegisterUserDto registerUserDto, String deviceId) {
        validateRegistrationRequest(registerUserDto);
        User newUser = createUser(registerUserDto);
        return generateAuthenticationResponse(newUser, deviceId);
    }

    @Override
    @Transactional
    public AuthenticationResponse authenticate(AuthenticateUserDto authenticateUserDto, String deviceId) {
        authenticateUserCredentials(authenticateUserDto);
        User user = retrieveUserByEmail(authenticateUserDto.getEmail());
        return generateAuthenticationResponse(user, deviceId);
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenOperationsDto refreshTokenOperationsDto, String deviceId) {
        User user = validateRefreshToken(refreshTokenOperationsDto.getRefreshToken(), deviceId);
        return generateAuthenticationResponse(user, deviceId);
    }

    @Override
    public void logout(RefreshTokenOperationsDto refreshTokenOperationsDto, String deviceId) {
        validateRefreshToken(refreshTokenOperationsDto.getRefreshToken(), deviceId);
        jwtService.revokeToken(refreshTokenOperationsDto.getRefreshToken());
        clearSecurityContext();
    }

    @Override
    public void logoutFromAllDevices(RefreshTokenOperationsDto refreshTokenOperationsDto) {
        User user = validateRefreshToken(refreshTokenOperationsDto.getRefreshToken());
        jwtService.revokeAllUserTokens(user);
        clearSecurityContext();
    }

    private void validateRegistrationRequest(RegisterUserDto registerUserDto) {
        registerUserDto.trimFields();
        if (userRepository.existsByEmailIgnoreCase(registerUserDto.getEmail())) {
            throw new ValidationException("validation.user.alreadyExists");
        }
    }

    private User createUser(RegisterUserDto registerUserDto) {
        User userToSave = userFactory.createFromRegisterDto(registerUserDto);
        try {
            User savedUser = userRepository.save(userToSave);
            log.info("Saved new user in repository with id = {}", savedUser.getId());
            return savedUser;
        } catch (Exception e) {
            log.error("Failed to save new user in repository", e);
            throw new SaveException("Failed to create new user", e);
        }
    }

    private void authenticateUserCredentials(AuthenticateUserDto authenticateUserDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticateUserDto.getEmail(),
                            authenticateUserDto.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new ValidationException("validation.user.invalidCredentials");
        }
    }

    private User retrieveUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ValidationException("auth.user.notFound"));
    }

    private User validateRefreshToken(String refreshToken, String deviceId) {
        User user = extractUserFromToken(refreshToken);
        if (!jwtService.isRefreshTokenValid(refreshToken, user, deviceId)) {
            throw new ValidationException("auth.token.invalid");
        }
        return user;
    }

    private User validateRefreshToken(String refreshToken) {
        User user = extractUserFromToken(refreshToken);
        if (!jwtService.isRefreshTokenValid(refreshToken, user)) {
            throw new ValidationException("auth.token.invalid");
        }
        return user;
    }

    private User extractUserFromToken(String token) {
        return (User) jwtService.extractUser(token);
    }

    private AuthenticationResponse generateAuthenticationResponse(User user, String deviceId) {
        String accessToken = jwtService.generateAccessToken(user, deviceId);
        String refreshToken = jwtService.generateRefreshToken(user, deviceId);
        return new AuthenticationResponse(accessToken, refreshToken);
    }

    private void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

}
