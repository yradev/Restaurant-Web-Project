package source.restaurant_web_project.service;

import source.restaurant_web_project.model.dto.NewReservationDTO;

public interface ReservationService {
    void reserve(NewReservationDTO newReservationDTO, String name);
}
