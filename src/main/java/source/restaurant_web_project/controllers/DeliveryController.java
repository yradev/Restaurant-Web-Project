package source.restaurant_web_project.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import source.restaurant_web_project.service.DeliveryService;
import source.restaurant_web_project.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("delivery")
public class DeliveryController {

    private final UserService userService;
    private final DeliveryService deliveryService;

    public DeliveryController(UserService userService, DeliveryService deliveryService) {
        this.userService = userService;
        this.deliveryService = deliveryService;
    }

    @GetMapping("new")
    public ModelAndView newDelivery(ModelAndView modelAndView, Principal principal){
        modelAndView.setViewName("delivery");
        modelAndView.addObject("addresses",userService.getUser(principal.getName()).getAddress());
        return modelAndView;
    }

    @PostMapping("new/processing")
    public String newDeliveryProcessing(@RequestParam String deliveryAddressName, Principal principal){
        deliveryService.addNewDelivery(deliveryAddressName,principal.getName());
        return "redirect:/user/deliveries";
    }

    @GetMapping("delete/{id}")
    public String cancelDelivery(@PathVariable long id){
        deliveryService.deleteDelivery(id);
        return "redirect:/user/deliveries";
    }
}
