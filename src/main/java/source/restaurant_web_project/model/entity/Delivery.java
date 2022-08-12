package source.restaurant_web_project.model.entity;

import source.restaurant_web_project.model.entity.enums.DeliveryStatus;
import source.restaurant_web_project.model.entity.superClass.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "deliveries")
public class Delivery extends BaseEntity {
    private LocalDateTime receiveTime;
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
    private String comment;
    private BigDecimal totalPrice;
    private boolean active;

    @ManyToOne(targetEntity = User.class)
    private User deliver;

    @ManyToMany(targetEntity = Item.class)
    @JoinTable(
            name = "delivery_item",
            joinColumns = @JoinColumn(name = "item_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "delivery_id",referencedColumnName = "id"))
    private Set<Item>items;

    @OneToOne
    private Address address;

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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
