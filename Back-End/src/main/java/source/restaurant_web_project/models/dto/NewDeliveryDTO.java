package source.restaurant_web_project.models.dto;

import source.restaurant_web_project.models.entity.enums.DeliveryStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class NewDeliveryDTO {
    private String deliveryAddressName;
    private LocalDateTime receiveTime;
    private DeliveryStatus status;
    private BigDecimal totalPrice;
    private boolean active;

    public NewDeliveryDTO(){}

    public String getDeliveryAddressName() {
        return deliveryAddressName;
    }

    public void setDeliveryAddressName(String deliveryAddressName) {
        this.deliveryAddressName = deliveryAddressName;
    }

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

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
