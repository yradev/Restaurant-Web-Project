package source.restaurant_web_project.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import source.restaurant_web_project.model.dto.NewReservationDTO;
import source.restaurant_web_project.service.ReservationService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("reservation")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @ModelAttribute("newReservationDTO")
    public NewReservationDTO newReservation(){
        return new NewReservationDTO();
    }

    @GetMapping
    public ModelAndView newReservation(ModelAndView modelAndView){
        modelAndView.setViewName("reservation");
        return modelAndView;
    }

    @PostMapping("new/processing")
    public String addNewReservation(@Valid NewReservationDTO newReservationDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, Principal principal){

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("newReservationDTO", newReservationDTO);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.newReservationDTO", bindingResult);
            redirectAttributes.addFlashAttribute("haveErrorAddCategory",true);
            return "redirect:/reservation";
        }

        reservationService.reserve(newReservationDTO,principal.getName());
        return "redirect:/user/reservations";
    }
}
