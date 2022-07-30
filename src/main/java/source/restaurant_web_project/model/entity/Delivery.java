package source.restaurant_web_project.model.entity;

import source.restaurant_web_project.model.entity.enums.DeliveryStatus;
import source.restaurant_web_project.model.entity.superClass.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "deliveries")
public class Delivery extends BaseEntity {
    private LocalDateTime receiveTime;
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;


    @ManyToOne(targetEntity = User.class)
    private User deliver;

    @ManyToMany(targetEntity = Item.class)
    @JoinTable(
            name = "delivery_item",
            joinColumns = @JoinColumn(name = "item_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "delivery_id",referencedColumnName = "id"))
    private Set<Item>items;

    public Delivery(){}

    public LocalDateTime getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(LocalDateTime receiveTime) {
        this.receiveTime = receiveTime;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public User getDeliver() {
        return deliver;
    }

    public void setDeliver(User deliver) {
        this.deliver = deliver;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }
}
