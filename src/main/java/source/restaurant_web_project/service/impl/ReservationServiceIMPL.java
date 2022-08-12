package source.restaurant_web_project.service.impl;

import org.springframework.stereotype.Service;
import source.restaurant_web_project.model.dto.NewReservationDTO;
import source.restaurant_web_project.model.entity.Reservation;
import source.restaurant_web_project.model.entity.User;
import source.restaurant_web_project.repository.ReservationRepository;
import source.restaurant_web_project.repository.UserRepository;
import source.restaurant_web_project.service.ReservationService;

@Service
public class ReservationServiceIMPL implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    public ReservationServiceIMPL(ReservationRepository reservationRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void reserve(NewReservationDTO newReservationDTO, String name) {
        Reservation reservation = new Reservation();
        User user = userRepository.findUserByUsername(name);
        reservation.setUser(user);
        reservationRepository.saveAndFlush(reservation);
    }
}
