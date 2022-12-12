package source.restaurant_web_project.models.dto.user;

public class UserChangeEmailDTO {
    private String currentEmail;
    private boolean emailIsFree;
    private String email;

    public UserChangeEmailDTO(){}

    public String getCurrentEmail() {
        return currentEmail;
    }

    public void setCurrentEmail(String currentEmail) {
        this.currentEmail = currentEmail;
    }

    public boolean isEmailIsFree() {
        return emailIsFree;
    }

    public void setEmailIsFree(boolean emailIsFree) {
        this.emailIsFree = emailIsFree;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
