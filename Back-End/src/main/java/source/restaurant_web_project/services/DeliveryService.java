package source.restaurant_web_project.services;

import source.restaurant_web_project.models.dto.NewDeliveryDTO;
import source.restaurant_web_project.models.dto.view.DeliveryViewDTO;
import source.restaurant_web_project.models.dto.view.ItemBagDTO;
import source.restaurant_web_project.models.entity.enums.DeliveryStatus;

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
