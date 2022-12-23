package source.restaurant_web_project.services;

import source.restaurant_web_project.models.dto.reservation.NewReservationDTO;
import source.restaurant_web_project.models.dto.reservation.ReservationViewDTO;

import java.util.List;

public interface ReservationService {
    long reserve(NewReservationDTO newReservationDTO);

    List<ReservationViewDTO> getReservationsForAuthenticatedUser();

    List<ReservationViewDTO> getStaffReservations();

    void changeReservationStatus(long reservationId, String status);

    ReservationViewDTO getReservation(long id);

}
