package source.restaurant_web_project.models.dto.user;

import java.util.List;

public class UserViewSettingsDTO {
    private String email;
    private List<UserAddressSettingsDTO> address;

    public UserViewSettingsDTO(){}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public List<UserAddressSettingsDTO> getAddress() {
        return address;
    }

    public void setAddress(List<UserAddressSettingsDTO> address) {
        this.address = address;
    }
}
