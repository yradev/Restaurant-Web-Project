package source.restaurant_web_project.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserRegisterDTO {
    @NotNull(message = "Username cannot be empty!")
    private String username;
    @Size(min = 5, message = "Password must be at least 5 symbols!")
    private String password;
    @Email
    @NotNull(message = "Email cannot be empty!")
    private String email;

    private String address;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
