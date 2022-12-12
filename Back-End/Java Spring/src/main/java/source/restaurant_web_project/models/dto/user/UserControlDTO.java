package source.restaurant_web_project.models.dto.user;

import source.restaurant_web_project.models.entity.Role;

import java.util.List;

public class UserControlDTO {
    private String username;
    private List<Role> roles;

    public UserControlDTO(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
