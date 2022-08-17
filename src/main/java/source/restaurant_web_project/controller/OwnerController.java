package source.restaurant_web_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import source.restaurant_web_project.configuration.RestaurantContextConfiguration.RestaurantConfiguration;
import source.restaurant_web_project.configuration.RestaurantContextConfiguration.RestaurantConfigurationDTO;
import source.restaurant_web_project.configuration.enums.DayOfWeeks;
import source.restaurant_web_project.configuration.enums.RestaurantStatus;
import source.restaurant_web_project.model.dto.item.CategoryAddDTO;
import source.restaurant_web_project.model.dto.item.CategoryEditDTO;
import source.restaurant_web_project.model.dto.item.ItemAddDTO;
import source.restaurant_web_project.model.dto.item.ItemEditDTO;
import source.restaurant_web_project.model.entity.Category;
import source.restaurant_web_project.model.entity.Item;
import source.restaurant_web_project.service.ItemService;
import source.restaurant_web_project.service.UserService;
import source.restaurant_web_project.util.DataValidator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("owner")
public class OwnerController {
    private final UserService userService;
    private final ItemService itemService;
    private final DataValidator dataValidator;
    private final RestaurantConfiguration restaurantConfiguration;

    public OwnerController(UserService userService, ItemService itemService, DataValidator dataValidator, RestaurantConfiguration restaurantConfiguration) {
        this.userService = userService;
        this.itemService = itemService;
        this.dataValidator = dataValidator;
        this.restaurantConfiguration = restaurantConfiguration;
    }

    @ModelAttribute("categoryAddDTO")
    public CategoryAddDTO categoryAddDTO(){
        return new CategoryAddDTO();
    }

    @ModelAttribute("categoryEditDTO")
    public CategoryEditDTO categoryEditDTO(){
        return new CategoryEditDTO();
    }

    @ModelAttribute("itemAddDTO")
    public ItemAddDTO itemAddDTO(){
        return new ItemAddDTO();
    }


    @ModelAttribute("categories")
    public List<Category> getCategories(){
        return itemService.getCategories().stream().limit(5).sorted(Comparator.comparingLong(Category::getPosition)).collect(Collectors.toList());
    }

    @ModelAttribute("items")
    public List<Item>items(){
        return new ArrayList<>();
    }

    @ModelAttribute("itemEditDTO")
    public ItemEditDTO itemEditDTO(){
        return new ItemEditDTO();
    }


    @GetMapping("menu")
    public ModelAndView getMenuControl(ModelAndView modelAndView){
        modelAndView.setViewName("control-panel/owner/menu-control");
        modelAndView.addObject("categoriesSize",itemService.getCategories().size());
        modelAndView.addObject("categoryPositionsIds",itemService.getCategories().stream().map(Category::getPosition).collect(Collectors.toList()));
        modelAndView.addObject("categoryNames",itemService.getCategories().stream().map(Category::getName).collect(Collectors.toList()));
        return modelAndView;
    }

    @PostMapping("menu/category/add/processing")
    public String postMenu(@Valid CategoryAddDTO categoryAddDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){

        bindingResult = dataValidator.validateCategoryAdd(categoryAddDTO, itemService.findCategory(categoryAddDTO.getName()), bindingResult);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("categoryAddDTO", categoryAddDTO);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.categoryAddDTO", bindingResult);
            redirectAttributes.addFlashAttribute("haveErrorAddCategory",true);
            return "redirect:/owner/menu";
        }

        itemService.addCategory(categoryAddDTO);
        return "redirect:/owner/menu";
    }

    @GetMapping("menu/category/get-view-page/{categoryViewPage}")
    public String getCategories(@PathVariable int categoryViewPage, RedirectAttributes redirectAttributes){
        switch (categoryViewPage) {
            case 1 -> redirectAttributes.addFlashAttribute("categories", itemService.getCategories().stream().limit(5).sorted(Comparator.comparingLong(Category::getPosition)).collect(Collectors.toList()));
            case 2 -> redirectAttributes.addFlashAttribute("categories", itemService.getCategories().stream().filter(a -> a.getPosition() > 5).limit(5).sorted(Comparator.comparingLong(Category::getPosition)).collect(Collectors.toList()));
            case 3 -> redirectAttributes.addFlashAttribute("categories", itemService.getCategories().stream().filter(a -> a.getPosition() > 10).limit(5).sorted(Comparator.comparingLong(Category::getPosition)).collect(Collectors.toList()));
            case 4 -> redirectAttributes.addFlashAttribute("categories", itemService.getCategories().stream().filter(a -> a.getPosition() > 15).limit(5).sorted(Comparator.comparingLong(Category::getPosition)).collect(Collectors.toList()));
        }
        return "redirect:/owner/menu";
    }

    @PostMapping("menu/category/edit/processing")
    public String editCategory(@Valid CategoryEditDTO categoryEditDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){

        bindingResult = dataValidator.validateCategoryEdit(bindingResult, categoryEditDTO(), itemService.findCategory(categoryEditDTO.getName()));

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("categoryEditDTO",categoryEditDTO);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.categoryEditDTO", bindingResult);
            redirectAttributes.addFlashAttribute("haveErrorEditCategory",true);
            return "redirect:/owner/menu";
        }
        itemService.editCategory(categoryEditDTO);
        return "redirect:/owner/menu";
    }

    @GetMapping("menu/category/delete/{categoryName}")
    public String deleteCategory(@PathVariable String categoryName){
        itemService.deleteCategory(categoryName);
        return "redirect:/owner/menu";
    }

    @PostMapping("menu/item/add/processing")
    public String addItem(@Valid ItemAddDTO itemAddDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes,@RequestParam int viewID){
        bindingResult = dataValidator.validateItemAdd(bindingResult, itemService.findItem(itemAddDTO.getName()));

        getItemViewAtributes(redirectAttributes, viewID, itemAddDTO.getCategoryName());

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("itemAddDTO", itemAddDTO);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.itemAddDTO", bindingResult);
            redirectAttributes.addFlashAttribute("haveErrorAddItem", true);
            return "redirect:/owner/menu";
        }


        itemService.addItem(itemAddDTO);
        return "redirect:/owner/menu/item/get-view-page/"+ viewID + "/" + itemAddDTO.getCategoryName();
    }

    @GetMapping("menu/item/get-view-page/{viewID}/{categoryName}")
    public String getItemsByCategoryName(@PathVariable int viewID, @PathVariable String categoryName,RedirectAttributes redirectAttributes){
        getItemViewAtributes(redirectAttributes, viewID, categoryName);
        return "redirect:/owner/menu";
    }

    private RedirectAttributes getItemViewAtributes(RedirectAttributes redirectAttributes,int viewID, String categoryName) {
        List<Item>items = itemService.getItemsByCategory(categoryName);
        if(items.isEmpty()){
            redirectAttributes.addFlashAttribute("categoryEmpty",true);
        }else {
            switch (viewID) {
                case 1 -> redirectAttributes.addFlashAttribute("items", items.stream().limit(5).sorted(Comparator.comparingLong(Item::getPosition)).collect(Collectors.toList()));
                case 2 -> redirectAttributes.addFlashAttribute("items", items.stream().filter(a -> a.getPosition() > 5).limit(5).sorted(Comparator.comparingLong(Item::getPosition)).collect(Collectors.toList()));
                case 3 -> redirectAttributes.addFlashAttribute("items", items.stream().filter(a -> a.getPosition() > 10).limit(5).sorted(Comparator.comparingLong(Item::getPosition)).collect(Collectors.toList()));
                case 4 -> redirectAttributes.addFlashAttribute("items", items.stream().filter(a -> a.getPosition() > 15).limit(5).sorted(Comparator.comparingLong(Item::getPosition)).collect(Collectors.toList()));
            }
        }

        redirectAttributes.addFlashAttribute("currentItemViewPosition",viewID);
        redirectAttributes.addFlashAttribute("itemPositionsIds",itemService.getItemsByCategory(categoryName).stream().map(Item::getPosition).collect(Collectors.toList()));
        redirectAttributes.addFlashAttribute("currentCategory",categoryName);
        redirectAttributes.addFlashAttribute("ItemsSize",itemService.getItemsByCategory(categoryName).size());
        redirectAttributes.addFlashAttribute("haveCategory", true);
        return redirectAttributes;
    }

    @GetMapping("menu/item/delete/{itemName}/{viewID}/{categoryName}")
    public String deleteItem(@PathVariable String itemName, @PathVariable int viewID, @PathVariable String categoryName){
        itemService.deleteItem(itemName,categoryName);
        return "redirect:/owner/menu/item/get-view-page/"+ viewID + "/" + categoryName;
    }

    @PostMapping("menu/item/edit/processing")
    public String editItem(@Valid ItemEditDTO itemEditDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        getItemViewAtributes(redirectAttributes, itemEditDTO.getCurrentViewPage(), itemEditDTO.getCurrentCategory());
        dataValidator.validateItemEdit(bindingResult,itemEditDTO,itemService.findItem(itemEditDTO.getName()));

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("itemEditDTO", itemEditDTO);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.itemEditDTO", bindingResult);
            redirectAttributes.addFlashAttribute("haveErrorEditItem", true);
            return "redirect:/owner/menu";
        }

        itemService.editItem(itemEditDTO);

        return "redirect:/owner/menu/item/get-view-page/"+ itemEditDTO.getCurrentViewPage() + "/" + itemEditDTO.getCurrentCategory();
    }


    @GetMapping("configurations")
    public ModelAndView restaurantConfiguration(ModelAndView modelAndView){
        modelAndView.setViewName("control-panel/owner/restaurant-configurations");
        modelAndView.addObject("Configurations",restaurantConfiguration.getConfigurations());
        modelAndView.addObject("statusValues", RestaurantStatus.values());
        modelAndView.addObject("dayOfWeeks", DayOfWeeks.values());
        return modelAndView;
    }

    @PostMapping("configurations/processing")
    public String configurationsProcessing(@Valid RestaurantConfigurationDTO restaurantConfigurationDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){


        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("haveError", true);
            return "redirect:/owner/configurations";
        }

        redirectAttributes.addFlashAttribute("configured",true);
        restaurantConfiguration.editConfigurations(restaurantConfigurationDTO);
        return "redirect:/owner/configurations";
    }
}

