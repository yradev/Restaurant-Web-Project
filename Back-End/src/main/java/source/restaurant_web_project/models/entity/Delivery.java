package source.restaurant_web_project.models.entity;

import source.restaurant_web_project.models.entity.enums.DeliveryStatus;
import source.restaurant_web_project.models.entity.superClass.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "deliveries")
public class Delivery extends BaseEntity {
    private LocalDateTime receiveTime;
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
    private BigDecimal totalPrice;

    @ManyToOne(targetEntity = User.class)
    private User deliver;

    @OneToMany(targetEntity = DeliveryItem.class)
    private Set<DeliveryItem> items;

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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Set<DeliveryItem> getItems() {
        return items;
    }

    public void setItems(Set<DeliveryItem> items) {
        this.items = items;
    }
}
