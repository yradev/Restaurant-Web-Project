package source.restaurant_web_project.model.dto.authentication;

import javax.validation.constraints.*;

public class UserRegisterDTO {
    @Size(min=3, max=10, message = "Username must be between 3 and 10 symbols!")
    private String username;
    @Size(min = 5, message = "Password must be at least 5 symbols!")
    private String password;
    private String confirmPassword;
    private String email;

    public UserRegisterDTO(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
