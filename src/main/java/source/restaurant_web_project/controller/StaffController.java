package source.restaurant_web_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import source.restaurant_web_project.model.dto.AddNewNewsDTO;
import source.restaurant_web_project.model.dto.item.LunchMenuAddDTO;
import source.restaurant_web_project.model.dto.view.DeliveryViewDTO;
import source.restaurant_web_project.model.dto.view.LunchMenuViewDTO;
import source.restaurant_web_project.model.dto.view.NewsViewDTO;
import source.restaurant_web_project.model.entity.enums.DeliveryStatus;
import source.restaurant_web_project.model.entity.enums.LunchMenuStatus;
import source.restaurant_web_project.service.DeliveryService;
import source.restaurant_web_project.service.ItemService;
import source.restaurant_web_project.service.LunchMenuService;
import source.restaurant_web_project.service.NewsService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("staff")
public class StaffController {

    private final DeliveryService deliveryService;
    private final ItemService itemService;
    private final LunchMenuService lunchMenuService;
    private final NewsService newsService;

    public StaffController(DeliveryService deliveryService, ItemService itemService, LunchMenuService lunchMenuService, NewsService newsService) {
        this.deliveryService = deliveryService;
        this.itemService = itemService;
        this.lunchMenuService = lunchMenuService;
        this.newsService = newsService;
    }


    @ModelAttribute("lunchMenuAddDTO")
    public LunchMenuAddDTO lunchMenuDTO(){
        return lunchMenuService.getLunchMenuAddDto();
    }

    @ModelAttribute("lunchMenuHistory")
    public List<LunchMenuViewDTO> viewHistory(){
        return lunchMenuService.getLunchHistory().stream().limit(9).collect(Collectors.toList());
    }

    @ModelAttribute("addNewNewsDTO")
    public AddNewNewsDTO addNewNewsDTO(){
        return new AddNewNewsDTO();
    }

    @ModelAttribute("historyNews")
    public List<NewsViewDTO> NewsViewDTO(){
        return newsService.getHistoryNews().stream().limit(8).collect(Collectors.toList());
    }

    @ModelAttribute("activeDeliveries")
    public List<DeliveryViewDTO> getEmptyActiveDeliveries(){
        return new ArrayList<>();
    }
    @ModelAttribute("historyDeliveries")
    public List<DeliveryViewDTO> getHistoryDeliveries(){
        return deliveryService.getDeliveriesForStaff().stream().filter(delivery->!delivery.isActive()).collect(Collectors.toList());
    }

    @GetMapping("deliveries")
    public ModelAndView getSettingsDelivery(ModelAndView modelAndView){
        modelAndView.setViewName("control-panel/staff/deliveries");
        modelAndView.addObject("getStaffActiveDeliveryStatus", deliveryService.getActiveDeliveriesNames());
        return modelAndView;
    }


    @GetMapping("lunch-menu")
    public ModelAndView lunchMenu(ModelAndView modelAndView){
        modelAndView.setViewName("control-panel/staff/lunch-menu");
        modelAndView.addObject("statusValues", LunchMenuStatus.values());
        return modelAndView;
    }

    @PostMapping("lunch-menu/processing")
    public String lunchMenuAdd(@Valid LunchMenuAddDTO lunchMenuAddDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("lunchMenuAddDTO", lunchMenuAddDTO);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.lunchMenuAddDTO", bindingResult);
            return "redirect:/staff/lunch-menu";
        }
        redirectAttributes.addFlashAttribute("added",true);
        lunchMenuService.addTodayLunchMenu(lunchMenuAddDTO);
        return "redirect:/staff/lunch-menu";
    }


    @GetMapping("/lunch-menu/history/get-view-page/{historyID}")
    public String getLunchMenuHistory(@PathVariable int historyID,RedirectAttributes redirectAttributes){
        switch (historyID) {
            case 1 -> redirectAttributes.addFlashAttribute("lunchMenuHistory", lunchMenuService.getLunchHistory().stream().limit(9).collect(Collectors.toList()));
            case 2 -> redirectAttributes.addFlashAttribute("lunchMenuHistory", lunchMenuService.getLunchHistory().stream().skip(9).limit(18).collect(Collectors.toList()));
            case 3 -> redirectAttributes.addFlashAttribute("lunchMenuHistory", lunchMenuService.getLunchHistory().stream().skip(18).limit(27).collect(Collectors.toList()));
            case 4 -> redirectAttributes.addFlashAttribute("lunchMenuHistory", lunchMenuService.getLunchHistory().stream().skip(27).limit(36).collect(Collectors.toList()));
            case 5 -> redirectAttributes.addFlashAttribute("lunchMenuHistory", lunchMenuService.getLunchHistory().stream().skip(36).limit(45).collect(Collectors.toList()));
        }
        return "redirect:/staff/lunch-menu";
    }

    @GetMapping("/lunch-menu/history/clear")
    public String clearHistory(){
        lunchMenuService.clearLunchMenuHistory();
        return "redirect:/staff/lunch-menu";
    }

    @GetMapping("/news")
    public ModelAndView getNews(ModelAndView modelAndView){
        modelAndView.setViewName("control-panel/staff/news");
        modelAndView.addObject("activeNews",newsService.getActiveNews());
        return modelAndView;
    }

    @PostMapping("/news/add/processing")
    public String addNews(@Valid AddNewNewsDTO addNewNewsDTO, BindingResult bindingResult,RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addNewNewsDTO", addNewNewsDTO);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.addNewNewsDTO", bindingResult);
            return "redirect:/staff/news";
        }

        newsService.addNews(addNewNewsDTO);
        return "redirect:/staff/news";
    }

    @GetMapping("news/delete/{id}")
    public String deleteNews(@PathVariable int id){
        newsService.deleteNews(id);
        return "redirect:/staff/news";
    }

    @GetMapping("news/history/get-view-page/{historyID}")
    public String historyNews(@PathVariable int historyID,RedirectAttributes redirectAttributes){
        switch (historyID) {
            case 1 -> redirectAttributes.addFlashAttribute("historyNews", newsService.getHistoryNews().stream().limit(8).collect(Collectors.toList()));
            case 2 -> redirectAttributes.addFlashAttribute("historyNews", newsService.getHistoryNews().stream().skip(8).limit(16).collect(Collectors.toList()));
            case 3 -> redirectAttributes.addFlashAttribute("historyNews", newsService.getHistoryNews().stream().skip(16).limit(24).collect(Collectors.toList()));
            case 4 -> redirectAttributes.addFlashAttribute("historyNews", newsService.getHistoryNews().stream().skip(24).limit(32).collect(Collectors.toList()));
            case 5 -> redirectAttributes.addFlashAttribute("historyNews", newsService.getHistoryNews().stream().skip(32).limit(40).collect(Collectors.toList()));
        }
        return "redirect:/staff/news";
    }

    @GetMapping("news/history/clear")
    public String clearNewsHistory(){
        newsService.clearHistory();
        return "redirect:/staff/news";
    }

    @GetMapping("deliveries/get/active/{deliveryStatus}/{viewID}")
    public String getActiveDeliveries(@PathVariable String deliveryStatus, @PathVariable int viewID,RedirectAttributes redirectAttributes){
       DeliveryStatus deliveryStatus1 = DeliveryStatus.valueOf(deliveryStatus);


        switch (viewID) {
            case 1 -> redirectAttributes.addFlashAttribute("activeDeliveries",
                            deliveryService.getDeliveriesForStaff().stream()
                                    .filter(delivery -> delivery.getDeliveryStatus().equals(deliveryStatus1) && delivery.isActive())
                                    .limit(10)
                                    .collect(Collectors.toList()));
        case 2 -> redirectAttributes.addFlashAttribute("activeDeliveries",
                            deliveryService.getDeliveriesForStaff().stream()
                                    .filter(delivery -> delivery.getDeliveryStatus().equals(deliveryStatus1) && delivery.isActive())
                                    .skip(10)
                                    .limit(20)
                                    .collect(Collectors.toList()));
        case 3 -> redirectAttributes.addFlashAttribute("activeDeliveries",
                            deliveryService.getDeliveriesForStaff().stream()
                                    .filter(delivery -> delivery.getDeliveryStatus().equals(deliveryStatus1) && delivery.isActive())
                                    .skip(20)
                                    .limit(30)
                                    .collect(Collectors.toList()));
        case 4 -> redirectAttributes.addFlashAttribute("activeDeliveries",
                            deliveryService.getDeliveriesForStaff().stream()
                                    .filter(delivery -> delivery.getDeliveryStatus().equals(deliveryStatus1) && delivery.isActive())
                                    .skip(30)
                                    .limit(40)
                                    .collect(Collectors.toList()));
        case 5 -> redirectAttributes.addFlashAttribute("activeDeliveries",
                            deliveryService.getDeliveriesForStaff().stream()
                                    .filter(delivery -> delivery.getDeliveryStatus().equals(deliveryStatus1) && delivery.isActive())
                                    .skip(40)
                                    .limit(10)
                                    .collect(Collectors.toList()));
        }

       redirectAttributes.addFlashAttribute("activeDeliveriesNotEmpty",true);
        redirectAttributes.addFlashAttribute("currentStatus",deliveryStatus);
        return "redirect:/staff/deliveries";
    }

    @GetMapping("deliveries/actives/get-view-page/{viewID}")
    public String getActiveDeliveriesPage(@PathVariable int viewID,RedirectAttributes redirectAttributes){
        switch (viewID) {
            case 1 -> redirectAttributes.addFlashAttribute("historyNews", newsService.getHistoryNews().stream().limit(8).collect(Collectors.toList()));
            case 2 -> redirectAttributes.addFlashAttribute("historyNews", newsService.getHistoryNews().stream().skip(8).limit(16).collect(Collectors.toList()));
            case 3 -> redirectAttributes.addFlashAttribute("historyNews", newsService.getHistoryNews().stream().skip(16).limit(24).collect(Collectors.toList()));
            case 4 -> redirectAttributes.addFlashAttribute("historyNews", newsService.getHistoryNews().stream().skip(24).limit(32).collect(Collectors.toList()));
            case 5 -> redirectAttributes.addFlashAttribute("historyNews", newsService.getHistoryNews().stream().skip(32).limit(40).collect(Collectors.toList()));
        }
        return "redirect:/staff/deliveries";
    }
}
