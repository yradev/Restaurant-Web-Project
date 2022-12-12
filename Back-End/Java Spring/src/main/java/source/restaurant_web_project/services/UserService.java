package source.restaurant_web_project.services;

import org.springframework.validation.BindingResult;
import source.restaurant_web_project.models.dto.user.*;
import source.restaurant_web_project.models.entity.User;

public interface UserService {
    User getUser(String name);

    void changeEmail(UserChangeEmailDTO userSettingsDTO, String name);

    void changeSettings(UserPasswordChangeDTO userSettingsDTO, String name);

    UserViewSettingsDTO getUserSettings(String name);

    boolean verifyUserByEmail(String email);

    BindingResult validateUserChangeEmail(UserChangeEmailDTO userChangeEmailDTO,BindingResult bindingResult, String name);

    BindingResult validateUserChangePassword(UserPasswordChangeDTO userPasswordChangeDTO, BindingResult bindingResult, String name);

    void changePassword(UserPasswordChangeDTO userPasswordChangeDTO, String name);

    BindingResult validateAddress(UserAddressSettingsDTO userAddressSettingsDTO, BindingResult bindingResult, String username);

    void addAddress(UserAddressSettingsDTO userAddressSettingsDTO, String name);

    void editAddress(UserAddressSettingsDTO userAddressSettingsDTO, BindingResult bindingResult, String name);

    UserControlDTO findUserForUserControl(String username);

    void addRole(String username, String role);

    void removeRole(String username, String role);
}
