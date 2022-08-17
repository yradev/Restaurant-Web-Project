package source.restaurant_web_project.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import source.restaurant_web_project.configuration.RestaurantContextConfiguration.RestaurantConfiguration;
import source.restaurant_web_project.configuration.RestaurantContextConfiguration.RestaurantConfigurationDTO;
import source.restaurant_web_project.model.dto.view.ItemBagDTO;
import source.restaurant_web_project.service.DeliveryService;

import java.util.List;

@ControllerAdvice
public class GlobalController {
    private final DeliveryService deliveryService;
    private final RestaurantConfiguration restaurantConfiguration;
    public GlobalController(DeliveryService deliveryService, RestaurantConfiguration restaurantConfiguration) {
        this.deliveryService = deliveryService;
        this.restaurantConfiguration = restaurantConfiguration;

    }

    @ModelAttribute("coreInformation")
    public RestaurantConfigurationDTO restaurantConfigurationDTO(){
        return restaurantConfiguration.getConfigurations();
    }

    @ModelAttribute("itemsBag")
    public List<ItemBagDTO> getItemsBag(){
        return deliveryService.getItemsFromBag();
    }

    @ModelAttribute("totalPriceOnItemsInBag")
    public Double getTotalPrice(){
        return deliveryService.getItemsFromBag().stream()
                .mapToDouble(a->a.getTotalPrice().doubleValue())
                .sum();
    }
}
