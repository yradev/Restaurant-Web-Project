package source.restaurant_web_project.service;

import source.restaurant_web_project.model.dto.view.DeliveryViewDTO;
import source.restaurant_web_project.model.dto.view.ItemBagDTO;
import source.restaurant_web_project.model.entity.enums.DeliveryStatus;

import java.math.BigDecimal;
import java.util.List;

public interface DeliveryService {
    boolean addItemToBag(String itemName, BigDecimal price);
    List<ItemBagDTO> getItemsFromBag();
    void deleteItemFromBag(String itemName,BigDecimal price);

    void addNewDelivery(String deliveryAddressName, String name);

    List<DeliveryViewDTO> getDeliveriesForCurrentUser(String name);

    void deleteDelivery(long id);

    List<DeliveryViewDTO> getDeliveriesForStaff();

    void addToHistory(long deliveryID, String status);

    List<DeliveryStatus> getActiveDeliveriesNames();
}
