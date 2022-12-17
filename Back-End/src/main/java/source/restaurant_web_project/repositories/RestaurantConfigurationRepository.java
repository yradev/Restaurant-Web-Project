package source.restaurant_web_project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import source.restaurant_web_project.models.entity.configuration.RestaurantConfigurationEntity;

@Repository
public interface RestaurantConfigurationRepository extends JpaRepository<RestaurantConfigurationEntity,Long> {
    RestaurantConfigurationEntity findById(long id);
}
