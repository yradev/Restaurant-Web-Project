package source.restaurant_web_project.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import source.restaurant_web_project.configuration.RestaurantContextConfiguration.RestaurantConfiguration;
import source.restaurant_web_project.model.dto.item.LunchMenuAddDTO;
import source.restaurant_web_project.service.LunchMenuService;
import source.restaurant_web_project.service.NewsService;
import source.restaurant_web_project.util.DataValidator;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
public class HomeController {

    private final DataValidator dataValidator;
    private final LunchMenuService lunchMenuService;
    private final NewsService newsService;
    private final RestaurantConfiguration restaurantConfiguration;

    public HomeController(DataValidator dataValidator, LunchMenuService lunchMenuService, NewsService newsService, RestaurantConfiguration restaurantConfiguration) {
        this.dataValidator = dataValidator;

        this.lunchMenuService = lunchMenuService;
        this.newsService = newsService;
        this.restaurantConfiguration = restaurantConfiguration;
    }

    @GetMapping("/")
    public ModelAndView Logged(ModelAndView modelAndView){
        modelAndView.addObject("todayLunchMenu",lunchMenuService.getTodayLunchMenu());
        modelAndView.addObject("news",newsService.getActiveNews());
        modelAndView.addObject("restaurantStatusValues", restaurantConfiguration.getConfigurations());
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @GetMapping("test")
    public String test(){
        LunchMenuAddDTO lunchMenuAddDTO = new LunchMenuAddDTO();
        lunchMenuAddDTO.setDateNow(LocalDate.now());
        lunchMenuAddDTO.setItemName1("Ovcharska salata");
        lunchMenuAddDTO.setItemName2("Drop sarma");
        lunchMenuAddDTO.setTotalPrice(BigDecimal.valueOf(12.50));
        lunchMenuService.addTodayLunchMenu(lunchMenuAddDTO);
        return "redirect:/";
    }

}
