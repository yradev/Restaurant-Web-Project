package source.restaurant_web_project.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import source.restaurant_web_project.models.dto.ChangeStatusDTO;
import source.restaurant_web_project.models.dto.reservation.NewReservationDTO;
import source.restaurant_web_project.models.dto.reservation.ReservationViewDTO;
import source.restaurant_web_project.services.ReservationService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<?>getReservationsForAuthenticatedUser(){
        return ResponseEntity.ok(reservationService.getReservationsForAuthenticatedUser());
    }

    @GetMapping("{id}")
    public ResponseEntity<ReservationViewDTO>getReservation(@PathVariable long id){
       return ResponseEntity.ok(reservationService.getReservation(id));
    }

    @GetMapping("add")
    public ResponseEntity<?>addReservation(@RequestBody NewReservationDTO newReservationDTO){
        return ResponseEntity.created(linkTo(methodOn(this.getClass())
                .getReservation(reservationService.reserve(newReservationDTO)))
                .toUri())
                .build();
    }

    @PreAuthorize("hasRole('ROLE_STAFF')")
    @GetMapping("staff")
    public ResponseEntity<?>getReservationsForStaff(){
        return ResponseEntity.ok(reservationService.getStaffReservations());
    }


    @PreAuthorize("hasRole('ROLE_STAFF')")
    @PutMapping("status/{id}")
    public ResponseEntity<?>changeRole(@PathVariable long id, @RequestBody ChangeStatusDTO changeStatusDTO){
        reservationService.changeReservationStatus(id,changeStatusDTO.getStatus());
        return ResponseEntity.ok().build();
    }


}
