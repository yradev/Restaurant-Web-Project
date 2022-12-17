package source.restaurant_web_project.models.dto.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.List;

public class UserEditDTO
{
    @Email
    private String email;
    private String oldPassword;
    @Size(min = 5, message = "Password must be at least 5 symbols!")
    private String newPassword;
    private List<AddressDTO> address;

    public UserEditDTO(){}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public List<AddressDTO> getAddress() {
        return address;
    }

    public void setAddress(List<AddressDTO> address) {
        this.address = address;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
