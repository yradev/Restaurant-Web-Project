package source.restaurant_web_project.model.entity;

import source.restaurant_web_project.model.entity.superClass.BaseEntity;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="categories")
public class Category extends BaseEntity {
    @Column(unique = true)
    private String name;

    private long position;
    @OneToMany(mappedBy = "category", targetEntity = Item.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }
}
