package source.restaurant_web_project.service;

import org.springframework.validation.BindingResult;
import source.restaurant_web_project.model.dto.UserSettingsDTO;
import source.restaurant_web_project.model.entity.User;

public interface UserService {
    User getUser(String name);

    void changeSettings(UserSettingsDTO userSettingsDTO, String name);
}
