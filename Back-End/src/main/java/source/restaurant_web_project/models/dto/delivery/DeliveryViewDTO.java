package source.restaurant_web_project.models.dto.delivery;

import source.restaurant_web_project.models.dto.item.ItemViewDTO;
import source.restaurant_web_project.models.dto.address.AddressVIewDTO;
import source.restaurant_web_project.models.entity.enums.DeliveryStatus;

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
