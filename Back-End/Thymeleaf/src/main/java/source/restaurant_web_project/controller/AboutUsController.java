package source.restaurant_web_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("about-us")
public class AboutUsController {
    @GetMapping
    public ModelAndView aboutUsView(ModelAndView modelAndView){
        modelAndView.setViewName("about-us");
        return modelAndView;
    }
}
