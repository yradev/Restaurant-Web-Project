package source.restaurant_web_project.models.dto.user;

import source.restaurant_web_project.models.entity.Role;

import java.util.List;

public class UserControlDTO {
    private boolean enabled;
    private List<RoleDTO>  roles;

    public UserControlDTO(){}

    public List<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDTO> roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
