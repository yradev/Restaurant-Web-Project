package source.restaurant_web_project.services;

import source.restaurant_web_project.models.dto.delivery.AddDeliveryDTO;
import source.restaurant_web_project.models.dto.delivery.DeliveryViewDTO;

import java.util.List;

public interface DeliveryService {

    long addNewDelivery(AddDeliveryDTO addDeliveryDTO);

    List<DeliveryViewDTO> getDeliveriesForAuthenticatedUser();

    List<DeliveryViewDTO> getDeliveriesForStaff();
    void changeStatus(long deliveryID, String status);

    DeliveryViewDTO getDeliveryDetails(Long id);
}
