package source.restaurant_web_project.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import source.restaurant_web_project.models.dto.configuration.RestaurantConfigurationDTO;
import source.restaurant_web_project.services.RestaurantConfiguration;
import source.restaurant_web_project.services.impl.RestaurantConfigurationIMPL;

import javax.validation.Valid;

@RestController
@RequestMapping("core")
public class RestaurantController {
    private final RestaurantConfiguration restaurantConfiguration;

    public RestaurantController(RestaurantConfiguration restaurantConfiguration) {
        this.restaurantConfiguration = restaurantConfiguration;
    }

    @GetMapping()
    public ResponseEntity<RestaurantConfigurationDTO>getRestaurantInformation(){
        RestaurantConfigurationDTO restaurantConfigurationDTO = restaurantConfiguration.getData();

        return ResponseEntity.ok(restaurantConfigurationDTO);
    }

    @PutMapping()
    public ResponseEntity<RestaurantConfigurationDTO>editRestaurantInformation(@RequestBody @Valid RestaurantConfigurationDTO restaurantConfigurationDTO){
        restaurantConfiguration.edit(restaurantConfigurationDTO);
        return ResponseEntity.ok().build();
    }
}
