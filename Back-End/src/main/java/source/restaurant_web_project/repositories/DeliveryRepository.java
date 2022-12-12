package source.restaurant_web_project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import source.restaurant_web_project.models.entity.Delivery;
import source.restaurant_web_project.models.entity.enums.DeliveryStatus;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery,Long> {
    List<Delivery>findDeliveriesByDeliver_Email(String email);
    Delivery findDeliveryById(long id);
    List<Delivery>findDeliveriesByStatus(DeliveryStatus deliveryStatus);
}
