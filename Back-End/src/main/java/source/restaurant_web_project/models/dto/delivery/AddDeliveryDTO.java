package source.restaurant_web_project.models.dto.delivery;

import java.time.LocalDateTime;
import java.util.List;

public class AddDeliveryDTO {
    private String deliveryAddressName;
    private LocalDateTime receivedTime;

    private List<DeliveryItemAdd> items;

    public AddDeliveryDTO(){}

    public String getDeliveryAddressName() {
        return deliveryAddressName;
    }

    public void setDeliveryAddressName(String deliveryAddressName) {
        this.deliveryAddressName = deliveryAddressName;
    }

    public LocalDateTime getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(LocalDateTime receivedTime) {
        this.receivedTime = receivedTime;
    }

    public List<DeliveryItemAdd> getItems() {
        return items;
    }

    public void setItems(List<DeliveryItemAdd> items) {
        this.items = items;
    }
}
