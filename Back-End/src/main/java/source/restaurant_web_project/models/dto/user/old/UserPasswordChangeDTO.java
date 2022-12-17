package source.restaurant_web_project.models.dto.user.old;

import javax.validation.constraints.Size;

public class UserPasswordChangeDTO {
    private String currentPassword;
    @Size(min = 5, message = "Password must be at least 5 symbols!")
    private String password;
    private String confirmPassword;

    public UserPasswordChangeDTO(){}

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
