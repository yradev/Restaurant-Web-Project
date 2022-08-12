package source.restaurant_web_project.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import source.restaurant_web_project.model.dto.view.ItemBagDTO;
import source.restaurant_web_project.service.DeliveryService;

import java.util.List;

@ControllerAdvice
public class GlobalController {
    private final DeliveryService deliveryService;

    public GlobalController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
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
