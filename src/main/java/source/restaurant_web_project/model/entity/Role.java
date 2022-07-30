package source.restaurant_web_project.model.entity;

import source.restaurant_web_project.model.entity.User;
import source.restaurant_web_project.model.entity.superClass.BaseEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity {
    private String name;

    @ManyToMany(mappedBy = "roles", targetEntity = User.class,fetch = FetchType.EAGER)
    private List<User> users;


    public Role() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
