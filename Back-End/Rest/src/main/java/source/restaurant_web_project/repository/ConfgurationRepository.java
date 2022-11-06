package source.restaurant_web_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import source.restaurant_web_project.model.entity.configuration.RestaurantConfigurationEntity;

@Repository
public interface ConfgurationRepository extends JpaRepository<RestaurantConfigurationEntity,Long> {
    RestaurantConfigurationEntity findById(long id);
}
