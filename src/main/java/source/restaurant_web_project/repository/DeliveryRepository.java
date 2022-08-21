package source.restaurant_web_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import source.restaurant_web_project.model.entity.Delivery;
import source.restaurant_web_project.model.entity.enums.DeliveryStatus;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery,Long> {
    List<Delivery>findDeliveriesByDeliver_Username(String username);
    Delivery findDeliveryById(long id);
    List<Delivery>findDeliveriesByStatus(DeliveryStatus deliveryStatus);
}
