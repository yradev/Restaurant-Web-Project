package source.restaurant_web_project.models.dto.user;

import org.springframework.hateoas.RepresentationModel;
import source.restaurant_web_project.models.entity.Role;

import java.util.List;

public class UserControlDTO extends RepresentationModel<UserControlDTO> {
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
