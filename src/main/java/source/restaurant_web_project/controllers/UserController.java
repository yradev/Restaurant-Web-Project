package source.restaurant_web_project.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import source.restaurant_web_project.model.dto.UserSettingsDTO;
import source.restaurant_web_project.model.entity.User;
import source.restaurant_web_project.service.UserService;
import source.restaurant_web_project.util.DataValidator;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    private final DataValidator validator;

    public UserController(UserService userService, DataValidator validator) {
        this.userService = userService;
        this.validator = validator;
    }

    @ModelAttribute("userSettingsDTO")
    public UserSettingsDTO getUserSettingsDTO(){
        return new UserSettingsDTO();
    }

    @GetMapping("settings")
    public ModelAndView getSettings(ModelAndView modelAndView, Principal principal){
        modelAndView.setViewName("user/settings");

       User user = userService.getUser(principal.getName());

        modelAndView.addObject("user",userService.getUser(principal.getName()));
        return modelAndView;
    }

    @PostMapping("settings/authentication")
    public String postSettings(@Valid UserSettingsDTO userSettingsDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, Principal principal){

        bindingResult = validator.validateSettings(userService.getUser(principal.getName()), bindingResult,userSettingsDTO);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userSettingsDTO", userSettingsDTO);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.userSettingsDTO", bindingResult);
            return "redirect:/user/settings";
        }

        userService.changeSettings(userSettingsDTO, principal.getName());
        redirectAttributes.addFlashAttribute("settingsChanged", true);
        return "redirect:/user/settings";
    }
}
