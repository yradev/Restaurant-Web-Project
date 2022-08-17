package source.restaurant_web_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import source.restaurant_web_project.model.dto.view.CategoryViewDTO;
import source.restaurant_web_project.model.dto.view.ItemViewDTO;
import source.restaurant_web_project.model.entity.Item;
import source.restaurant_web_project.service.DeliveryService;
import source.restaurant_web_project.service.ItemService;
import source.restaurant_web_project.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("menu")
public class MenuController {
    private final ItemService itemService;
    private final DeliveryService deliveryService;
    private final UserService userService;

    public MenuController(ItemService itemService, DeliveryService deliveryService, UserService userService) {
        this.itemService = itemService;
        this.deliveryService = deliveryService;
        this.userService = userService;
    }

    @ModelAttribute("categories")
    public List<CategoryViewDTO> categoryViewDTO(){
        return itemService.getCategoryNameAndDescription();
    }

    @ModelAttribute("itemsLeft")
    public List<ItemViewDTO>getItemsLeft(){
        return new ArrayList<>();
    }
    @ModelAttribute("itemsRight")
    public List<ItemViewDTO>getItemsRight(){
        return new ArrayList<>();
    }

    @GetMapping
    public ModelAndView getMenu(ModelAndView modelAndView){
        modelAndView.setViewName("menu");
        return modelAndView;
    }

    @GetMapping("items/get/{categoryName}")
    public String getItems(@PathVariable String categoryName, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("itemsLeft",itemService.getItemsView(categoryName).stream().limit(10).collect(Collectors.toList()));
        redirectAttributes.addFlashAttribute("itemsRight",itemService.getItemsView(categoryName).stream().skip(10).limit(10).collect(Collectors.toList()));
        redirectAttributes.addFlashAttribute("currentCategory",categoryName);
        return "redirect:/menu";
    }


    @GetMapping("items/add/bag/{itemName}")
    public String addCookieToBag(@PathVariable String itemName,RedirectAttributes redirectAttributes){
        Item item = itemService.findItem(itemName);
        deliveryService.addItemToBag(itemName,item.getPrice());
        if(!deliveryService.addItemToBag(itemName,item.getPrice())){
            redirectAttributes.addFlashAttribute("bagIsFull",true);
        }

        if(itemName.equals("Lunch menu")){
            return "redirect:/";
        }
        String categoryName = item.getCategory().getName();
        return "redirect:/menu/items/get/"+categoryName;
    }

    @GetMapping("items/delete/bag/{itemName}")
    public String deleteCookieFromBag(@PathVariable String itemName){
        deliveryService.deleteItemFromBag(itemName,itemService.findItem(itemName).getPrice());
        String categoryName = itemService.findItem(itemName).getCategory().getName();
        return "redirect:/menu/items/get/"+categoryName;
    }
}
