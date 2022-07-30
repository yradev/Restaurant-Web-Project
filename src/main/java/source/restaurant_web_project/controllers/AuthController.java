package source.restaurant_web_project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import source.restaurant_web_project.model.dto.UserRegisterDTO;
import source.restaurant_web_project.model.entity.User;
import source.restaurant_web_project.service.AuthService;
import source.restaurant_web_project.util.EmailSender;
import source.restaurant_web_project.util.Validator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.MalformedURLException;

@Controller
@RequestMapping(value = "auth",method = {RequestMethod.GET, RequestMethod.POST})
public class AuthController {

    private final AuthService authService;
    private final EmailSender emailSender;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AuthController(HttpServletRequest httpServletRequest, AuthService authService, EmailSender emailSender, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.authService = authService;
        this.emailSender = emailSender;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

//   #########################
//              Login
//    ########################

    @GetMapping(value = "login")
    public String login(){
        if(Validator.isUserLogged()){
            return "redirect:/";
        }

        return "authentication/login";
    }

    @PostMapping("login/error")
    public String loginErr(@RequestParam String username, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("wrongInput",true);
        redirectAttributes.addFlashAttribute("username",username);
        return "redirect:/auth/login";
    }

    //   #########################
    //         Password Reset
    //    ########################

    @GetMapping("password-reset")
    public String getForgotPassword(){
        return "authentication/password-reset-email";
    }

    @PostMapping("password-reset/authentication")
    public String postForgotPassword(@RequestParam String email, HttpServletRequest request,RedirectAttributes redirectAttributes) throws MalformedURLException {
        User user = authService.findUserByEmail(email);
        if (user == null) {
            redirectAttributes.addFlashAttribute("userNotFound",true);
            return "redirect:/auth/password-reset";
        }

        if(authService.checkTokenExist(email)){
            redirectAttributes.addFlashAttribute("tokenExists",true);
            return "redirect:/auth/password-reset";
        }

        emailSender.send(email,"Reset Password",authService.getMessageWithToken(user,request));
        redirectAttributes.addFlashAttribute("tokenSend",true);
        return "redirect:/auth/password-reset";
    }

    @GetMapping("password-reset/verifiy")
    public ModelAndView tokenVerifyResetPassword(@RequestParam String token, @RequestParam String email, ModelAndView modelAndView,RedirectAttributes redirectAttributes){
        User user = authService.findUserByEmail(email);
        if(user==null || !authService.verifyToken(token,email)){
            redirectAttributes.addFlashAttribute("badToken", true);
            modelAndView.setViewName("redirect:/auth/password-reset");
            return modelAndView;
        }

        modelAndView.addObject("email",email);
        modelAndView.addObject("token",token);
        modelAndView.setViewName("authentication/password-reset-change-password");

        return modelAndView;
    }

    @PostMapping("password-reset/verifiy/authentication")
    public String test(@RequestParam String token,@RequestParam String email, @RequestParam String password, @RequestParam String confirmPassword, RedirectAttributes redirectAttributes){

        if(!password.equals(confirmPassword)){
            redirectAttributes.addFlashAttribute("confirmPasswordError",true);
            return "redirect:/auth/password-reset/verifiy?token="+ token + "&email=" + email;
        }

        if(password.length()<5){
            redirectAttributes.addFlashAttribute("passwordLengthError",true);
            return "redirect:/auth/password-reset/verifiy?token="+ token + "&email=" + email;
        }

        authService.restorePassword(password,email);

        redirectAttributes.addFlashAttribute("passwordChanged",true);

        return "redirect:/auth/login";
    }

    //   #########################
    //            Register
    //   ########################

    @GetMapping("register")
    public String getRegister(){

        if(Validator.isUserLogged()){
            return "redirect:/";
        }

        return "authentication/register";
    }



    @ModelAttribute("userRegisterDTO")
    public UserRegisterDTO userRegisterDTO(){
        return new UserRegisterDTO();
    }

    @PostMapping("/auth/register")
    public String postRegister(@Valid UserRegisterDTO userRegisterDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        return null;
    }

}
