package source.restaurant_web_project.models.dto.user;

import java.util.List;

public class UserDataSendDTO {
    private String email;
    private List<AddressDTO> address;
    private List<RoleDTO> roles;

    public UserDataSendDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<AddressDTO> getAddress() {
        return address;
    }

    public void setAddress(List<AddressDTO> address) {
        this.address = address;
    }

    public List<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDTO> roles) {
        this.roles = roles;
    }
}
