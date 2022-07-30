package source.restaurant_web_project.model.entity;

import source.restaurant_web_project.model.entity.superClass.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name="categories")
public class Category extends BaseEntity {
    @Column(unique = true)
    private String name;
    @Column(columnDefinition = "text")
    private String description;

    @OneToMany(mappedBy = "category",targetEntity = Item.class)
    private Set<Item> items;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }
}
