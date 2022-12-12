package source.restaurant_web_project.models.dto.authentication;

public class VerifyResetPasswordTokenDTO {
    private String email;
    private String tooken;

    public VerifyResetPasswordTokenDTO(){}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTooken() {
        return tooken;
    }

    public void setTooken(String tooken) {
        this.tooken = tooken;
    }
}
