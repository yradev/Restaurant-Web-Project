package source.restaurant_web_project.service;

import source.restaurant_web_project.model.dto.NewDeliveryDTO;
import source.restaurant_web_project.model.dto.view.DeliveryViewDTO;
import source.restaurant_web_project.model.dto.view.ItemBagDTO;
import source.restaurant_web_project.model.entity.enums.DeliveryStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface DeliveryService {
    boolean addItemToBag(String itemName, BigDecimal price);
    List<ItemBagDTO> getItemsFromBag();
    void deleteItemFromBag(String itemName);

    void addNewDelivery(NewDeliveryDTO newDeliveryDTO,String name);

    List<DeliveryViewDTO> getDeliveriesForCurrentUser(String name);

    void deleteDelivery(long id);

    List<DeliveryViewDTO> getDeliveriesForStaff();

    void addToHistory(long deliveryID, String status);

    List<DeliveryStatus> getActiveDeliveriesNames();

    void changeStatus(long deliveryID, String status);

    Map<String,Integer> getSizeOfActiveDeliveries();

    void clearStaffDeliveryHistory();
}
