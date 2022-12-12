package source.restaurant_web_project.models.entity;

import source.restaurant_web_project.models.entity.superClass.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "items")
public class Item extends BaseEntity {
    @Column(unique = true)
    private String name;
    @Column(columnDefinition = "text")
    private String description;
    private BigDecimal price;
    private long position;
    private long countInBag;

    @ManyToOne(targetEntity = Category.class, fetch = FetchType.EAGER)
    private Category category;

    @ManyToMany(mappedBy = "items",targetEntity = Delivery.class)
    private Set<Delivery> deliveries;

    public Item(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Set<Delivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(Set<Delivery> deliveries) {
        this.deliveries = deliveries;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }


    public long getCountInBag() {
        return countInBag;
    }

    public void setCountInBag(long countInBag) {
        this.countInBag = countInBag;
    }
}
