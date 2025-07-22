package pl.osetoctet.user;

import pl.osetoctet.user.model.dto.ChangePasswordDto;
import pl.osetoctet.user.model.entity.User;

public interface UserProfileService {

    void changePassword(User user, ChangePasswordDto changePasswordDto);

    void deactivateAccount(User user);

}
