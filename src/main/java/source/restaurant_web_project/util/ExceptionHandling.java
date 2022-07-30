package source.restaurant_web_project.util;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandling{

    @ExceptionHandler(SecurityException.class)
    public void something(SecurityException e){
        System.out.println(e.getMessage());
    }
}
