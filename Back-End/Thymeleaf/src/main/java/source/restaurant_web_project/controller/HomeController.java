package source.restaurant_web_project.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import source.restaurant_web_project.configuration.RestaurantConfiguration;
import source.restaurant_web_project.model.dto.view.DeliveryViewDTO;
import source.restaurant_web_project.model.dto.view.ReservationViewDTO;
import source.restaurant_web_project.service.DeliveryService;
import source.restaurant_web_project.service.LunchMenuService;
import source.restaurant_web_project.service.NewsService;
import source.restaurant_web_project.service.ReservationService;
import source.restaurant_web_project.util.DataValidator;

import java.security.Principal;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final DataValidator dataValidator;
    private final LunchMenuService lunchMenuService;
    private final NewsService newsService;
    private final RestaurantConfiguration restaurantConfiguration;
    private final DeliveryService deliveryService;
    private final ReservationService reservationService;

    public HomeController(DataValidator dataValidator, LunchMenuService lunchMenuService, NewsService newsService, RestaurantConfiguration restaurantConfiguration, DeliveryService deliveryService, ReservationService reservationService) {
        this.dataValidator = dataValidator;

        this.lunchMenuService = lunchMenuService;
        this.newsService = newsService;
        this.restaurantConfiguration = restaurantConfiguration;
        this.deliveryService = deliveryService;
        this.reservationService = reservationService;
    }

    @GetMapping("/")
    public ModelAndView getIndexPage(ModelAndView modelAndView, Principal principal){
        modelAndView.addObject("todayLunchMenu",lunchMenuService.getTodayLunchMenu());
        modelAndView.addObject("news",newsService.getActiveNews());
        modelAndView.addObject("restaurantStatusValues", restaurantConfiguration.getConfigurations());
        if(principal!=null) {
            modelAndView.addObject("activeReservations", reservationService.getCurrentUserReservations(principal.getName()).stream()
                    .filter(ReservationViewDTO::isActive)
                    .collect(Collectors.toList()));
            modelAndView.addObject("activeDeliveries", deliveryService.getDeliveriesForCurrentUser(principal.getName()).stream()
                    .filter(DeliveryViewDTO::isActive)
                    .collect(Collectors.toList()));
        }
        modelAndView.setViewName("index");
        return modelAndView;
    }

}
