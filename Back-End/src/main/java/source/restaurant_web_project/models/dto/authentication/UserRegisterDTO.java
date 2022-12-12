package source.restaurant_web_project.models.dto.authentication;

import javax.validation.constraints.*;

public class UserRegisterDTO {
    @Email
    private String email;
    @Size(min = 5, message = "Password must be at least 5 symbols!")
    private String password;

    public UserRegisterDTO(){}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
