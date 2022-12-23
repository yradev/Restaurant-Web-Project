package source.restaurant_web_project.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import source.restaurant_web_project.errors.NotFoundException;
import source.restaurant_web_project.models.dto.reservation.NewReservationDTO;
import source.restaurant_web_project.models.dto.reservation.ReservationViewDTO;
import source.restaurant_web_project.models.entity.Reservation;
import source.restaurant_web_project.models.entity.User;
import source.restaurant_web_project.models.entity.enums.ReservationStatus;
import source.restaurant_web_project.repositories.ReservationRepository;
import source.restaurant_web_project.repositories.UserRepository;
import source.restaurant_web_project.services.ReservationService;

import java.util.List;
import java.util.stream.Collectors;

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
    public long reserve(NewReservationDTO newReservationDTO) {
        Reservation reservation = modelMapper.map(newReservationDTO, Reservation.class);
        User user = userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setUser(user);
        reservationRepository.saveAndFlush(reservation);
        return 0;
    }

    @Override
    public List<ReservationViewDTO> getReservationsForAuthenticatedUser() {
        return reservationRepository.findReservationsByUser_Email(SecurityContextHolder.getContext().getAuthentication().getName()).stream()
                .map(reservation -> modelMapper.map(reservation,ReservationViewDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationViewDTO> getStaffReservations() {
        return reservationRepository.findAll().stream()
                .map(reservation -> modelMapper.map(reservation,ReservationViewDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void changeReservationStatus(long reservationId, String status) {
        Reservation reservation = reservationRepository.findById(reservationId).get();
        ReservationStatus reservationStatus = ReservationStatus.valueOf(status);

        reservation.setStatus(reservationStatus);
        reservationRepository.saveAndFlush(reservation);
    }

    @Override
    public ReservationViewDTO getReservation(long id) {
        Reservation reservation = reservationRepository.findReservationById(id);
        if(reservation == null){
            throw new NotFoundException();
        }
        return modelMapper.map(reservation,ReservationViewDTO.class);
    }
}
