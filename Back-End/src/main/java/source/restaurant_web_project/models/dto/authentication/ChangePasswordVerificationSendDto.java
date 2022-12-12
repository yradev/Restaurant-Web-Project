package source.restaurant_web_project.models.dto.authentication;

public class ChangePasswordVerificationSendDto {
    private String email;
    private String url;

    public ChangePasswordVerificationSendDto(){}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
