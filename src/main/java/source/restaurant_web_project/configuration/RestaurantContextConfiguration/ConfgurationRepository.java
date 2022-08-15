package source.restaurant_web_project.configuration.RestaurantContextConfiguration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfgurationRepository extends JpaRepository<RestaurantConfigurationEntity,Long> {
    RestaurantConfigurationEntity findById(long id);
}
