package source.restaurant_web_project.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import source.restaurant_web_project.model.dto.user.UserAddressSettingsDTO;
import source.restaurant_web_project.model.dto.user.UserChangeEmailDTO;
import source.restaurant_web_project.model.dto.user.UserPasswordChangeDTO;
import source.restaurant_web_project.model.dto.user.UserViewSettingsDTO;
import source.restaurant_web_project.model.dto.view.DeliveryViewDTO;
import source.restaurant_web_project.service.DeliveryService;
import source.restaurant_web_project.service.UserService;
import source.restaurant_web_project.util.DataValidator;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.stream.Collectors;

@Controller
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    private final DataValidator validator;
    private final DeliveryService deliveryService;

    public UserController(UserService userService, DataValidator validator, DeliveryService deliveryService) {
        this.userService = userService;
        this.validator = validator;
        this.deliveryService = deliveryService;
    }

    @ModelAttribute("changeEmail")
    public UserChangeEmailDTO getUserSettingsDTO(){
        return new UserChangeEmailDTO();
    }

    @ModelAttribute("changePassword")
    public UserPasswordChangeDTO changePassword(){
        return new UserPasswordChangeDTO();
    }

    @ModelAttribute("addressDTO")
    public UserAddressSettingsDTO addressSettingsDTO(){
        return new UserAddressSettingsDTO();
    }


    @ModelAttribute("user")
    public UserViewSettingsDTO getUserViewSettings(Principal principal){
        return userService.getUserSettings(principal.getName());
    }

    @GetMapping("settings")
    public ModelAndView getSettings(ModelAndView modelAndView, Principal principal){
        modelAndView.setViewName("control-panel/user/settings");

        return modelAndView;
    }

    @PostMapping("settings/change/email/processing")
    public String changeEmail(@Valid UserChangeEmailDTO userChangeEmailDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, Principal principal,
                              @RequestParam String currentEmail){
        userChangeEmailDTO.setCurrentEmail(currentEmail);

        if(!userChangeEmailDTO.getEmail().equals("")) {
            userChangeEmailDTO.setEmailIsFree(userService.verifyUserByEmail(userChangeEmailDTO.getEmail()));
        }

        bindingResult = userService.validateUserChangeEmail(userChangeEmailDTO,bindingResult, principal.getName());

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("changeEmail", userChangeEmailDTO);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.changeEmail", bindingResult);
            redirectAttributes.addFlashAttribute("haveErrorChangeEmail",true);
            return "redirect:/user/settings";
        }

        userService.changeEmail(userChangeEmailDTO,principal.getName());
        return "redirect:/user/settings";
    }

    @PostMapping("settings/change/password/processing")
    public String changeEmail(@Valid UserPasswordChangeDTO userPasswordChangeDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, Principal principal){

        bindingResult = userService.validateUserChangePassword(userPasswordChangeDTO,bindingResult, principal.getName());

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("changePassword", userPasswordChangeDTO);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.changePassword", bindingResult);
            redirectAttributes.addFlashAttribute("haveErrorChangePassword",true);
            return "redirect:/user/settings";
        }

        userService.changePassword(userPasswordChangeDTO,principal.getName());
        return "redirect:/user/settings";
    }

    @PostMapping("add/address/processing")
    public String addAddress(@Valid UserAddressSettingsDTO userAddressSettingsDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, Principal principal){

        bindingResult = userService.validateAddress(userAddressSettingsDTO,bindingResult, principal.getName());

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addressDTO", userAddressSettingsDTO);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.addressDTO", bindingResult);
            redirectAttributes.addFlashAttribute("haveErrorAddAddress",true);
            return "redirect:/user/settings";
        }

        userService.addAddress(userAddressSettingsDTO,principal.getName());

        return "redirect:/user/settings";
    }

    @PostMapping("/settings/edit/address")
    public String editAddress(@Valid UserAddressSettingsDTO userAddressSettingsDTO,BindingResult bindingResult, RedirectAttributes redirectAttributes, Principal principal){

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addressDTO", userAddressSettingsDTO);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.addressDTO", bindingResult);
            redirectAttributes.addFlashAttribute("haveErrorAddAddress",true);
            return "redirect:/user/settings";
        }

        userService.editAddress(userAddressSettingsDTO,bindingResult,principal.getName());
        return "redirect:/user/settings";
    }

    @GetMapping("deliveries")
    public ModelAndView modelAndView(ModelAndView modelAndView,Principal principal){
        modelAndView.setViewName("control-panel/user/deliveries");
        modelAndView.addObject("activeDeliveries",
                deliveryService.getDeliveriesForCurrentUser(principal.getName()).stream()
                        .filter(DeliveryViewDTO::isActive)
                        .collect(Collectors.toList()));

        modelAndView.addObject("historyDeliveries",
                deliveryService.getDeliveriesForCurrentUser(principal.getName()).stream()
                        .filter(delivery->!delivery.isActive())
                        .collect(Collectors.toList()));

        modelAndView.addObject("addresses",userService.getUser(principal.getName()).getAddress());
        return modelAndView;
    }

    @PostMapping("deliveries/new/processing")
    public String newDeliveryProcessing(@RequestParam String deliveryAddressName, Principal principal){
        deliveryService.addNewDelivery(deliveryAddressName,principal.getName());
        return "redirect:/user/deliveries";
    }


    @GetMapping("deliveries/delete/{id}")
    public String cancelDelivery(@PathVariable long id){
        deliveryService.deleteDelivery(id);
        return "redirect:/user/deliveries";
    }
}
