package source.restaurant_web_project.service;

import source.restaurant_web_project.model.dto.NewReservationDTO;
import source.restaurant_web_project.model.dto.view.ReservationViewDTO;

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
