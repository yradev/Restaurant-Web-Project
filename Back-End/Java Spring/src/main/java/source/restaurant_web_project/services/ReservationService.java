package source.restaurant_web_project.services;

import source.restaurant_web_project.models.dto.NewReservationDTO;
import source.restaurant_web_project.models.dto.view.ReservationViewDTO;

import java.util.List;
import java.util.Map;

public interface ReservationService {
    void reserve(NewReservationDTO newReservationDTO, String name);

    List<ReservationViewDTO> getCurrentUserReservations(String username);

    void deleteReservation(long reservationId);

    List<ReservationViewDTO> getStaffReservations();

    Map<String, Integer> getSizeOfActiveReservations();

    void changeReservationStatus(long reservationId, String status);

}
