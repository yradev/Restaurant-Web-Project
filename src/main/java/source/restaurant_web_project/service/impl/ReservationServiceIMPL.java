package source.restaurant_web_project.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import source.restaurant_web_project.model.dto.NewReservationDTO;
import source.restaurant_web_project.model.entity.Reservation;
import source.restaurant_web_project.model.entity.User;
import source.restaurant_web_project.model.entity.enums.ReservationStatus;
import source.restaurant_web_project.repository.ReservationRepository;
import source.restaurant_web_project.repository.UserRepository;
import source.restaurant_web_project.service.ReservationService;

@Service
public class ReservationServiceIMPL implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public ReservationServiceIMPL(ReservationRepository reservationRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void reserve(NewReservationDTO newReservationDTO, String name) {
        Reservation reservation = modelMapper.map(newReservationDTO, Reservation.class);
        User user = userRepository.findUserByUsername(name);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setUser(user);
        reservationRepository.saveAndFlush(reservation);
    }
}
