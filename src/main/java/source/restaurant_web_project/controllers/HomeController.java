package source.restaurant_web_project.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

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
