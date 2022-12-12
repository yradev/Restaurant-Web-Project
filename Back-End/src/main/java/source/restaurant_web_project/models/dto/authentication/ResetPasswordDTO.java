package source.restaurant_web_project.models.dto.authentication;

import javax.validation.constraints.Size;

public class ResetPasswordDTO {
    @Size(min = 5, message = "Password must be at least 5 symbols!")
    private String password;

    public ResetPasswordDTO(){};

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
