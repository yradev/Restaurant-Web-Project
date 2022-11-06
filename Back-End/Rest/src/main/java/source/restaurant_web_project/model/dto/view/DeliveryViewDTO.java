package source.restaurant_web_project.model.dto.view;

import source.restaurant_web_project.model.entity.enums.DeliveryStatus;

import javax.xml.stream.events.Comment;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DeliveryViewDTO {
    private long id;
    private String receiveTime;
    private String deliverUsername;
    private List<ItemViewDTO>items;
    private BigDecimal totalPrice;
    private DeliveryStatus deliveryStatus;
    private AddressVIewDTO deliveryAddress;
    private Comment comment;
    private boolean active;

    public DeliveryViewDTO(){}

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(LocalDateTime receiveTime) {
        this.receiveTime = receiveTime.format(DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm"));
    }

    public List<ItemViewDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemViewDTO> items) {
        this.items = items;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public AddressVIewDTO getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(AddressVIewDTO deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDeliverUsername() {
        return deliverUsername;
    }

    public void setDeliverUsername(String deliverUsername) {
        this.deliverUsername = deliverUsername;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
