package source.restaurant_web_project.services;

import source.restaurant_web_project.models.dto.configuration.RestaurantConfigurationDTO;
import source.restaurant_web_project.models.entity.configuration.RestaurantConfigurationEntity;

public interface RestaurantConfiguration {
    RestaurantConfigurationDTO getData();
    void edit(RestaurantConfigurationDTO restaurantConfigurationDTO);
}
