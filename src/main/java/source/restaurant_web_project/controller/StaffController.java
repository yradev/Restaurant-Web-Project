package source.restaurant_web_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import source.restaurant_web_project.model.dto.AddNewNewsDTO;
import source.restaurant_web_project.model.dto.item.LunchMenuAddDTO;
import source.restaurant_web_project.model.dto.view.DeliveryViewDTO;
import source.restaurant_web_project.model.dto.view.LunchMenuViewDTO;
import source.restaurant_web_project.model.dto.view.NewsViewDTO;
import source.restaurant_web_project.model.dto.view.ReservationViewDTO;
import source.restaurant_web_project.model.entity.enums.DeliveryStatus;
import source.restaurant_web_project.model.entity.enums.LunchMenuStatus;
import source.restaurant_web_project.model.entity.enums.ReservationStatus;
import source.restaurant_web_project.service.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("staff")
public class StaffController {

    private final DeliveryService deliveryService;
    private final ItemService itemService;
    private final LunchMenuService lunchMenuService;
    private final NewsService newsService;
    private final ReservationService reservationService;

    public StaffController(DeliveryService deliveryService, ItemService itemService, LunchMenuService lunchMenuService, NewsService newsService, ReservationService reservationService) {
        this.deliveryService = deliveryService;
        this.itemService = itemService;
        this.lunchMenuService = lunchMenuService;
        this.newsService = newsService;
        this.reservationService = reservationService;
    }


    @ModelAttribute("lunchMenuAddDTO")
    public LunchMenuAddDTO lunchMenuDTO() {
        return lunchMenuService.getLunchMenuAddDto();
    }

    @ModelAttribute("lunchMenuHistory")
    public List<LunchMenuViewDTO> viewHistory() {
        return lunchMenuService.getLunchHistory().stream().limit(9).collect(Collectors.toList());
    }

    @ModelAttribute("addNewNewsDTO")
    public AddNewNewsDTO addNewNewsDTO() {
        return new AddNewNewsDTO();
    }

    @ModelAttribute("historyNews")
    public List<NewsViewDTO> NewsViewDTO() {
        return newsService.getHistoryNews().stream().limit(8).collect(Collectors.toList());
    }

    @ModelAttribute("activeDeliveries")
    public List<DeliveryViewDTO> getEmptyActiveDeliveries() {
        return new ArrayList<>();
    }

    @ModelAttribute("historyDeliveries")
    public List<DeliveryViewDTO> getHistoryDeliveries() {
        return deliveryService.getDeliveriesForStaff().stream().filter(delivery -> !delivery.isActive()).collect(Collectors.toList());
    }

    @ModelAttribute("historyReservations")
    public List<ReservationViewDTO> getHistoryReservations() {
        return reservationService.getStaffReservations().stream().filter(reservation -> !reservation.isActive()).collect(Collectors.toList());
    }

    @ModelAttribute("activeReservations")
    public List<ReservationViewDTO> activeReservations() {
        return new ArrayList<>();
    }

    @ModelAttribute("historyDeliveriesSize")
    public int getHistoryDeliveriesSize() {
        return 0;
    }

    @GetMapping("deliveries")
    public ModelAndView getSettingsDelivery(ModelAndView modelAndView) {
        modelAndView.setViewName("control-panel/staff/deliveries");
        modelAndView.addObject("getStaffActiveDeliveryStatus", deliveryService.getActiveDeliveriesNames());
        modelAndView.addObject("refusedStatus", DeliveryStatus.REFUSED);
        modelAndView.addObject("sizeOfActiveDeliveries", deliveryService.getSizeOfActiveDeliveries());
        return modelAndView;
    }


    @GetMapping("lunch-menu")
    public ModelAndView lunchMenu(ModelAndView modelAndView) {
        modelAndView.setViewName("control-panel/staff/lunch-menu");
        modelAndView.addObject("statusValues", LunchMenuStatus.values());
        return modelAndView;
    }

    @PostMapping("lunch-menu/processing")
    public String lunchMenuAdd(@Valid LunchMenuAddDTO lunchMenuAddDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("lunchMenuAddDTO", lunchMenuAddDTO);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.lunchMenuAddDTO", bindingResult);
            return "redirect:/staff/lunch-menu";
        }
        redirectAttributes.addFlashAttribute("added", true);
        lunchMenuService.addTodayLunchMenu(lunchMenuAddDTO);
        return "redirect:/staff/lunch-menu";
    }


    @GetMapping("/lunch-menu/history/get-view-page/{historyID}")
    public String getLunchMenuHistory(@PathVariable int historyID, RedirectAttributes redirectAttributes) {
        switch (historyID) {
            case 1 -> redirectAttributes.addFlashAttribute("lunchMenuHistory", lunchMenuService.getLunchHistory().stream().limit(9).collect(Collectors.toList()));
            case 2 -> redirectAttributes.addFlashAttribute("lunchMenuHistory", lunchMenuService.getLunchHistory().stream().skip(9).limit(18).collect(Collectors.toList()));
            case 3 -> redirectAttributes.addFlashAttribute("lunchMenuHistory", lunchMenuService.getLunchHistory().stream().skip(18).limit(27).collect(Collectors.toList()));
            case 4 -> redirectAttributes.addFlashAttribute("lunchMenuHistory", lunchMenuService.getLunchHistory().stream().skip(27).limit(36).collect(Collectors.toList()));
            case 5 -> redirectAttributes.addFlashAttribute("lunchMenuHistory", lunchMenuService.getLunchHistory().stream().skip(36).limit(45).collect(Collectors.toList()));
        }
        return "redirect:/staff/lunch-menu";
    }

    @GetMapping("/lunch-menu/history/clear")
    public String clearHistory() {
        lunchMenuService.clearLunchMenuHistory();
        return "redirect:/staff/lunch-menu";
    }

    @GetMapping("/news")
    public ModelAndView getNews(ModelAndView modelAndView) {
        modelAndView.setViewName("control-panel/staff/news");
        modelAndView.addObject("activeNews", newsService.getActiveNews());
        return modelAndView;
    }

    @PostMapping("/news/add/processing")
    public String addNews(@Valid AddNewNewsDTO addNewNewsDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addNewNewsDTO", addNewNewsDTO);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.addNewNewsDTO", bindingResult);
            return "redirect:/staff/news";
        }

        newsService.addNews(addNewNewsDTO);
        return "redirect:/staff/news";
    }

    @GetMapping("news/delete/{id}")
    public String deleteNews(@PathVariable int id) {
        newsService.deleteNews(id);
        return "redirect:/staff/news";
    }

    @GetMapping("news/history/get-view-page/{historyID}")
    public String historyNews(@PathVariable int historyID, RedirectAttributes redirectAttributes) {
        switch (historyID) {
            case 1 -> redirectAttributes.addFlashAttribute("historyNews", newsService.getHistoryNews().stream().limit(8).collect(Collectors.toList()));
            case 2 -> redirectAttributes.addFlashAttribute("historyNews", newsService.getHistoryNews().stream().skip(8).limit(16).collect(Collectors.toList()));
            case 3 -> redirectAttributes.addFlashAttribute("historyNews", newsService.getHistoryNews().stream().skip(16).limit(24).collect(Collectors.toList()));
            case 4 -> redirectAttributes.addFlashAttribute("historyNews", newsService.getHistoryNews().stream().skip(24).limit(32).collect(Collectors.toList()));
            case 5 -> redirectAttributes.addFlashAttribute("historyNews", newsService.getHistoryNews().stream().skip(32).limit(40).collect(Collectors.toList()));
        }
        return "redirect:/staff/news";
    }

    @GetMapping("news/history/clear")
    public String clearNewsHistory() {
        newsService.clearHistory();
        return "redirect:/staff/news";
    }

    @GetMapping("deliveries/get/active/{deliveryStatus}/{viewID}")
    public String getActiveDeliveries(@PathVariable String deliveryStatus, @PathVariable int viewID, RedirectAttributes redirectAttributes) {
        DeliveryStatus deliveryStatus1 = DeliveryStatus.valueOf(deliveryStatus);

        List<DeliveryViewDTO> deliveries = deliveryService.getDeliveriesForStaff().stream()
                .filter(delivery -> delivery.getDeliveryStatus().equals(deliveryStatus1) && delivery.isActive())
                .collect(Collectors.toList());

        switch (viewID) {
            case 1 -> redirectAttributes.addFlashAttribute("activeDeliveries",
                    deliveries.stream()
                            .limit(9)
                            .collect(Collectors.toList()));
            case 2 -> redirectAttributes.addFlashAttribute("activeDeliveries",
                    deliveries.stream()
                            .skip(9)
                            .limit(18)
                            .collect(Collectors.toList()));
            case 3 -> redirectAttributes.addFlashAttribute("activeDeliveries",
                    deliveries.stream()
                            .skip(18)
                            .limit(27)
                            .collect(Collectors.toList()));
            case 4 -> redirectAttributes.addFlashAttribute("activeDeliveries",
                    deliveries.stream()
                            .skip(27)
                            .limit(36)
                            .collect(Collectors.toList()));
            case 5 -> redirectAttributes.addFlashAttribute("activeDeliveries",
                    deliveries.stream()
                            .skip(36)
                            .limit(45)
                            .collect(Collectors.toList()));
        }

        redirectAttributes.addFlashAttribute("activeDeliveriesSize", deliveries.size());
        redirectAttributes.addFlashAttribute("activeDeliveriesNotEmpty", true);
        redirectAttributes.addFlashAttribute("currentStatus", deliveryStatus);
        return "redirect:/staff/deliveries";
    }

    @GetMapping("deliveries/history/get-view-page/{viewID}")
    public String getHistoryViewPages(@PathVariable int viewID, RedirectAttributes redirectAttributes) {
        List<DeliveryViewDTO> deliveries = deliveryService.getDeliveriesForStaff().stream()
                .filter(delivery -> !delivery.isActive())
                .collect(Collectors.toList());

        switch (viewID) {
            case 1 -> redirectAttributes.addFlashAttribute("historyDeliveries", deliveries.stream()
                    .limit(10)
                    .collect(Collectors.toList()));
            case 2 -> redirectAttributes.addFlashAttribute("historyDeliveries", deliveries.stream()
                    .skip(10)
                    .limit(20)
                    .collect(Collectors.toList()));
            case 3 -> redirectAttributes.addFlashAttribute("historyDeliveries", deliveries.stream()
                    .skip(20)
                    .limit(30)
                    .collect(Collectors.toList()));
            case 4 -> redirectAttributes.addFlashAttribute("historyDeliveries", deliveries.stream()
                    .skip(30)
                    .limit(40)
                    .collect(Collectors.toList()));
            case 5 -> redirectAttributes.addFlashAttribute("historyDeliveries", deliveries.stream()
                    .skip(40)
                    .limit(50)
                    .collect(Collectors.toList()));
        }


        redirectAttributes.addFlashAttribute("historyDeliveriesSize", deliveries.size());
        return "redirect:/staff/deliveries";
    }

    @GetMapping("deliveries/change-status/{deliveryID}/{status}")
    public String changeDeliveryStatus(@PathVariable long deliveryID, @PathVariable String status) {
        deliveryService.changeStatus(deliveryID, status);
        return "redirect:/staff/deliveries";
    }

    @GetMapping("reservations")
    public ModelAndView getReservations(ModelAndView modelAndView) {
        modelAndView.setViewName("control-panel/staff/reservations");
        modelAndView.addObject("reservationActiveStatuses", List.of(ReservationStatus.PENDING, ReservationStatus.ACCEPTED));
        modelAndView.addObject("reservationStatusSizes", reservationService.getSizeOfActiveReservations());
        modelAndView.addObject("canceledStatus", ReservationStatus.CANCELED.name());
        modelAndView.addObject("visitedStatus",ReservationStatus.VISITED);
        modelAndView.addObject("historyReservationsSize", reservationService.getStaffReservations().size());
        return modelAndView;
    }

    @GetMapping("reservations/get/active/{reservationStatus}/{viewID}")
    public String getViewPageActiveReservationWithStatus(@PathVariable String reservationStatus, @PathVariable int viewID, RedirectAttributes redirectAttributes) {
        ReservationStatus status = ReservationStatus.valueOf(reservationStatus);
        List<ReservationViewDTO> reservations = reservationService.getStaffReservations().stream()
                .filter(reservation -> reservation.getStatus().equals(status) && reservation.isActive())
                .collect(Collectors.toList());

        switch (viewID) {
            case 1 -> redirectAttributes.addFlashAttribute("activeReservations", reservations.stream()
                    .limit(10)
                    .collect(Collectors.toList()));
            case 2 -> redirectAttributes.addFlashAttribute("activeReservations", reservations.stream()
                    .skip(10)
                    .limit(20)
                    .collect(Collectors.toList()));
            case 3 -> redirectAttributes.addFlashAttribute("activeReservations", reservations.stream()
                    .skip(20)
                    .limit(30)
                    .collect(Collectors.toList()));
            case 4 -> redirectAttributes.addFlashAttribute("activeReservations", reservations.stream()
                    .skip(30)
                    .limit(40)
                    .collect(Collectors.toList()));
            case 5 -> redirectAttributes.addFlashAttribute("activeReservations", reservations.stream()
                    .skip(40)
                    .limit(50)
                    .collect(Collectors.toList()));
        }

        redirectAttributes.addFlashAttribute("activeReservationsNotEmpty", true);
        redirectAttributes.addFlashAttribute("currentStatus", status);
        redirectAttributes.addFlashAttribute("activeReservationsSize", reservations.size());


        return "redirect:/staff/reservations";
    }

    @GetMapping("reservations/change-status/{reservationId}/{status}")
    public String changeStatusReservation(@PathVariable long reservationId, @PathVariable String status) {
        reservationService.changeReservationStatus(reservationId, status);
        return "redirect:/staff/reservations/";
    }

    @GetMapping("reservations/history/get-view-page/{viewID}")
    public String getViewPageActiveReservationWithStatus(@PathVariable int viewID, RedirectAttributes redirectAttributes) {
        List<ReservationViewDTO> reservations = reservationService.getStaffReservations().stream()
                .filter(reservation -> !reservation.isActive())
                .collect(Collectors.toList());

        switch (viewID) {
            case 1 -> redirectAttributes.addFlashAttribute("historyReservations", reservations.stream()
                    .limit(10)
                    .collect(Collectors.toList()));
            case 2 -> redirectAttributes.addFlashAttribute("historyReservations", reservations.stream()
                    .skip(10)
                    .limit(20)
                    .collect(Collectors.toList()));
            case 3 -> redirectAttributes.addFlashAttribute("historyReservations", reservations.stream()
                    .skip(20)
                    .limit(30)
                    .collect(Collectors.toList()));
            case 4 -> redirectAttributes.addFlashAttribute("historyReservations", reservations.stream()
                    .skip(30)
                    .limit(40)
                    .collect(Collectors.toList()));
            case 5 -> redirectAttributes.addFlashAttribute("historyReservations", reservations.stream()
                    .skip(40)
                    .limit(50)
                    .collect(Collectors.toList()));
        }
        return "redirect:/staff/reservations";
    }
}
