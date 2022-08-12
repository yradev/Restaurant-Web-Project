package source.restaurant_web_project.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import source.restaurant_web_project.model.dto.view.DeliveryViewDTO;
import source.restaurant_web_project.service.DeliveryService;

import java.util.stream.Collectors;

@Controller
@RequestMapping("staff")
public class StaffController {

    private final DeliveryService deliveryService;

    public StaffController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping("deliveries")
    public ModelAndView getSettingsDelivery(ModelAndView modelAndView){
        modelAndView.setViewName("settings/staff/deliveries");
        modelAndView.addObject("activeDeliveries",deliveryService.getDeliveriesForStaff().stream().filter(DeliveryViewDTO::isActive).collect(Collectors.toList()));
        modelAndView.addObject("HistoryDeliveries",deliveryService.getDeliveriesForStaff().stream().filter(delivery->!delivery.isActive()).collect(Collectors.toList()));
        return modelAndView;
    }
}
