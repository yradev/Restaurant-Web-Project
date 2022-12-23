package source.restaurant_web_project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import source.restaurant_web_project.models.entity.DeliveryItem;

public interface DeliveryItemRepository extends JpaRepository<DeliveryItem,Long> {
}
