package pl.osetoctet.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.osetoctet.common.exception.SaveException;
import pl.osetoctet.common.exception.ValidationException;
import pl.osetoctet.user.model.dto.ChangePasswordDto;
import pl.osetoctet.user.model.dto.RefreshTokenOperationsDto;
import pl.osetoctet.user.model.entity.User;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationServiceImpl authenticationService;

    @Override
    @Transactional
    public void changePassword(User user, ChangePasswordDto changePasswordDto) {
        validatePasswordChange(user, changePasswordDto);
        updatePassword(user, changePasswordDto.getNewPassword());
        persistUserPassword(user);
        logoutFromAllDevices(changePasswordDto.getRefreshToken());
    }

    private void validatePasswordChange(User user, ChangePasswordDto changePasswordDto) {
        validateCurrentPassword(user, changePasswordDto.getCurrentPassword());
        validateNewPassword(user, changePasswordDto.getNewPassword());
    }

    private void validateCurrentPassword(User user, String currentPassword) {
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new ValidationException("validation.changePassword.incorrect");
        }
    }

    private void validateNewPassword(User user, String newPassword) {
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new ValidationException("validation.changePassword.cannotBeTheSame");
        }
    }

    private void updatePassword(User user, String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
    }

    private void persistUserPassword(User user) {
        try {
            userRepository.save(user);
            log.info("Changed password for user with id: {}", user.getId());
        } catch (Exception e) {
            log.error("Failed to change password for user with id: {}", user.getId(), e);
            throw new SaveException("Failed to change password", e);
        }
    }

    private void logoutFromAllDevices(String refreshToken) {
        RefreshTokenOperationsDto refreshTokenOperationsDto = new RefreshTokenOperationsDto();
        refreshTokenOperationsDto.setRefreshToken(refreshToken);
        authenticationService.logoutFromAllDevices(refreshTokenOperationsDto);
    }

    @Override
    @Transactional
    public void deactivateAccount(User user) {
        log.info("Deactivating user with id: {}", user.getId());
        try {
            Optional<User> userEntity = userRepository.findById(user.getId());
            if (userEntity.isPresent()) {
                User userToDeactivate = userEntity.get();
                userRepository.delete(userToDeactivate);
                log.info("Deactivated user with email: {}", user.getEmail());
            }
        } catch (Exception e) {
            log.error("Failed to deactivate user with id: {}", user.getId(), e);
            throw new SaveException("Failed to deactivate user", e);
        }
    }

}
