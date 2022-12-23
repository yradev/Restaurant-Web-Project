package source.restaurant_web_project.services;

import source.restaurant_web_project.models.dto.user.RoleDTO;
import source.restaurant_web_project.models.dto.user.UserControlDTO;
import source.restaurant_web_project.models.dto.user.UserDataViewDTO;
import source.restaurant_web_project.models.dto.user.UserEditDTO;

import java.util.List;

public interface UserService {

    UserDataViewDTO getUserData(String email);

    void editUserData(UserEditDTO userEditDTO);

    List<RoleDTO> getActiveRoles();

    void changeUserCoreData(String email,UserControlDTO userControlDTO);

    UserControlDTO getUserCoreSettings(String email);
}
