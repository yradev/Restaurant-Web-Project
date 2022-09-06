package source.restaurant_web_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import source.restaurant_web_project.model.dto.AddNewNewsDTO;
import source.restaurant_web_project.model.dto.NewDeliveryDTO;
import source.restaurant_web_project.model.dto.user.UserAddressSettingsDTO;
import source.restaurant_web_project.model.dto.user.UserChangeEmailDTO;
import source.restaurant_web_project.model.dto.user.UserPasswordChangeDTO;
import source.restaurant_web_project.model.dto.user.UserViewSettingsDTO;
import source.restaurant_web_project.model.dto.view.DeliveryViewDTO;
import source.restaurant_web_project.model.dto.view.ReservationViewDTO;
import source.restaurant_web_project.service.DeliveryService;
import source.restaurant_web_project.service.ReservationService;
import source.restaurant_web_project.service.UserService;
import source.restaurant_web_project.util.DataValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    private final DataValidator validator;
    private final DeliveryService deliveryService;
    private final ReservationService reservationService;

    public UserController(UserService userService, DataValidator validator, DeliveryService deliveryService, ReservationService reservationService) {
        this.userService = userService;
        this.validator = validator;
        this.deliveryService = deliveryService;
        this.reservationService = reservationService;
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

    @ModelAttribute("historyDeliveries")
    public List<DeliveryViewDTO> deliveryViewDTO(Principal principal){
        return deliveryService.getDeliveriesForCurrentUser(principal.getName()).stream()
                .filter(delivery->!delivery.isActive())
                .limit(9)
                .collect(Collectors.toList());
    }

    @GetMapping("settings")
    public ModelAndView getSettings(ModelAndView modelAndView){
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

    @ModelAttribute("newDeliveryDTO")
    public NewDeliveryDTO newDeliveryDTO(){
        return new NewDeliveryDTO();
    }

    @GetMapping("deliveries")
    public ModelAndView modelAndView(ModelAndView modelAndView,Principal principal){
        modelAndView.setViewName("control-panel/user/deliveries");
        modelAndView.addObject("activeDeliveries",
                deliveryService.getDeliveriesForCurrentUser(principal.getName()).stream()
                        .filter(DeliveryViewDTO::isActive)
                        .collect(Collectors.toList()));

        modelAndView.addObject("addresses",userService.getUser(principal.getName()).getAddress());
        modelAndView.addObject("historySize",deliveryService.getDeliveriesForCurrentUser(principal.getName()).size());
        return modelAndView;
    }

    @PostMapping("deliveries/new/processing")
    public String newDeliveryProcessing(@Valid NewDeliveryDTO newDeliveryDTO, BindingResult bindingResult,RedirectAttributes redirectAttributes, Principal principal){

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("newDeliveryDTO", newDeliveryDTO);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.newDeliveryDTO", bindingResult);
            return "redirect:/user/settings";
        }
        deliveryService.addNewDelivery(newDeliveryDTO,principal.getName());
        return "redirect:/user/deliveries";
    }


    @GetMapping("deliveries/delete/{deliveryID}")
    public String cancelDelivery(@PathVariable long deliveryID) {
        deliveryService.deleteDelivery(deliveryID);
        return "redirect:/user/deliveries";
    }

    @GetMapping("deliveries/add-to-history/{deliveryID}/{status}")
    public String addToHistory(@PathVariable long deliveryID, @PathVariable String status){
        deliveryService.addToHistory(deliveryID,status);
        return "redirect:/user/deliveries";
    }

    @GetMapping("deliveries/history/get-view-page/{viewID}")
    public String getDeliveryHistory(@PathVariable int viewID,RedirectAttributes redirectAttributes, Principal principal){
        switch (viewID){
        case 1 -> redirectAttributes.addFlashAttribute("historyDeliveries",
                deliveryService.getDeliveriesForCurrentUser(principal.getName()).stream()
                        .filter(delivery->!delivery.isActive())
                        .limit(9)
                        .collect(Collectors.toList()));

        case 2 -> redirectAttributes.addFlashAttribute("historyDeliveries",
                deliveryService.getDeliveriesForCurrentUser(principal.getName()).stream()
                        .filter(delivery->!delivery.isActive())
                        .skip(9)
                        .limit(18)
                        .collect(Collectors.toList()));

        case 3 -> redirectAttributes.addFlashAttribute("historyDeliveries",
                deliveryService.getDeliveriesForCurrentUser(principal.getName()).stream()
                        .filter(delivery->!delivery.isActive())
                        .skip(18)
                        .limit(27)
                        .collect(Collectors.toList()));

        case 4 -> redirectAttributes.addFlashAttribute("historyDeliveries",
                deliveryService.getDeliveriesForCurrentUser(principal.getName()).stream()
                        .filter(delivery->!delivery.isActive())
                        .skip(27)
                        .limit(36)
                        .collect(Collectors.toList()));

        case 5 -> redirectAttributes.addFlashAttribute("historyDeliveries",
                deliveryService.getDeliveriesForCurrentUser(principal.getName()).stream()
                        .filter(delivery->!delivery.isActive())
                        .skip(36)
                        .limit(45)
                        .collect(Collectors.toList()));
        }

        return "redirect:/user/deliveries";
    }

    @GetMapping("reservations")
    public ModelAndView getReservations(ModelAndView modelAndView, Principal principal){
        modelAndView.setViewName("control-panel/user/reservations");
        modelAndView.addObject("activeReservation",reservationService.getCurrentUserReservations(principal.getName()).stream()
                .filter(ReservationViewDTO::isActive)
                .collect(Collectors.toList()));
        modelAndView.addObject("history",reservationService.getCurrentUserReservations(principal.getName()).stream()
                .filter(reservation -> !reservation.isActive())
                .collect(Collectors.toList()));
        return modelAndView;
    }

    @GetMapping("reservations/delete/{reservationId}")
    public String deleteReservation(@PathVariable long reservationId){
        reservationService.deleteReservation(reservationId);
        return "redirect:/user/reservations";
    }

}
