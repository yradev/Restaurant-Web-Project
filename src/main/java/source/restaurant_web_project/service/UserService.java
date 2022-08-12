package source.restaurant_web_project.service;

import org.springframework.validation.BindingResult;
import source.restaurant_web_project.model.dto.user.UserAddressSettingsDTO;
import source.restaurant_web_project.model.dto.user.UserChangeEmailDTO;
import source.restaurant_web_project.model.dto.user.UserPasswordChangeDTO;
import source.restaurant_web_project.model.dto.user.UserViewSettingsDTO;
import source.restaurant_web_project.model.entity.User;

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
}
