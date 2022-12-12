package source.restaurant_web_project.models.dto.authentication;

public class UserLoginDTO {
    private String email;
    private String password;

    public UserLoginDTO(){};


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
