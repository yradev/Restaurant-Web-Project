package source.restaurant_web_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import source.restaurant_web_project.model.dto.NewReservationDTO;
import source.restaurant_web_project.model.dto.view.ReservationViewDTO;
import source.restaurant_web_project.service.ReservationService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.stream.Collectors;

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
    public ModelAndView newReservation(ModelAndView modelAndView,Principal principal){
        modelAndView.setViewName("reservation");
        return modelAndView;
    }

    @PostMapping("new/processing")
    public String addNewReservation(@Valid NewReservationDTO newReservationDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, Principal principal){

        if(reservationService.getCurrentUserReservations(principal.getName()).stream().anyMatch(ReservationViewDTO::isActive)) {
            redirectAttributes.addFlashAttribute("tooMuchActiveReservations", true);
            return "redirect:/reservation";
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("newReservationDTO", newReservationDTO);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.newReservationDTO", bindingResult);
            return "redirect:/reservation";
        }

        reservationService.reserve(newReservationDTO,principal.getName());
        return "redirect:/user/reservations";
    }
}
