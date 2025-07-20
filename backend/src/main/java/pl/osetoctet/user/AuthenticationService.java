package pl.osetoctet.user;

import pl.osetoctet.user.model.dto.AuthenticateUserDto;
import pl.osetoctet.user.model.dto.AuthenticationResponse;
import pl.osetoctet.user.model.dto.RefreshTokenOperationsDto;
import pl.osetoctet.user.model.dto.RegisterUserDto;

public interface AuthenticationService {

    AuthenticationResponse register(RegisterUserDto registerUserDto, String deviceId);

    AuthenticationResponse authenticate(AuthenticateUserDto authenticateUserDto, String deviceId);

    AuthenticationResponse refreshToken(RefreshTokenOperationsDto refreshTokenOperationsDto, String deviceId);

    void logout(RefreshTokenOperationsDto refreshTokenOperationsDto, String deviceId);

    void logoutFromAllDevices(RefreshTokenOperationsDto refreshTokenOperationsDto);

}
