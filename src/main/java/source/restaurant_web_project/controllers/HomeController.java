package source.restaurant_web_project.controllers;


//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import source.restaurant_web_project.model.entity.User;
import source.restaurant_web_project.util.Validator;

@Controller
public class HomeController {

    @GetMapping("/")
    public ModelAndView Logged(ModelAndView modelAndView){
        modelAndView.setViewName("index");
        return modelAndView;
    }
    @GetMapping("drop")
    public String a(){
        throw new IllegalArgumentException("qwrqwr");
    }

}
