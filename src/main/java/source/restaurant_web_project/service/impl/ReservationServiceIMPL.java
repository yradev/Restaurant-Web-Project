package source.restaurant_web_project.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import source.restaurant_web_project.model.dto.NewReservationDTO;
import source.restaurant_web_project.model.dto.view.ReservationViewDTO;
import source.restaurant_web_project.model.entity.Reservation;
import source.restaurant_web_project.model.entity.User;
import source.restaurant_web_project.model.entity.enums.ReservationStatus;
import source.restaurant_web_project.repository.ReservationRepository;
import source.restaurant_web_project.repository.UserRepository;
import source.restaurant_web_project.service.ReservationService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
    public void reserve(NewReservationDTO newReservationDTO, String name) {
        Reservation reservation = modelMapper.map(newReservationDTO, Reservation.class);
        User user = userRepository.findUserByUsername(name);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setUser(user);
        reservation.setActive(true);
        reservationRepository.saveAndFlush(reservation);
    }

    @Override
    public List<ReservationViewDTO> getCurrentUserReservations(String username) {
        return reservationRepository.findReservationsByUser_Username(username).stream()
                .map(reservation -> modelMapper.map(reservation,ReservationViewDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteReservation(long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).get();
        if(reservation.getStatus().equals(ReservationStatus.PENDING)){
            reservationRepository.delete(reservation);
        }
    }

    @Override
    public List<ReservationViewDTO> getStaffReservations() {
        return reservationRepository.findAll().stream().map(reservation -> modelMapper.map(reservation,ReservationViewDTO.class)).collect(Collectors.toList());
    }

    @Override
    public Map<String, Integer> getSizeOfActiveReservations() {
        Map<String,Integer> sizeOfActiveDeliveries = new LinkedHashMap<>();
        for (ReservationStatus reservationStatus : List.of(ReservationStatus.PENDING,ReservationStatus.ACCEPTED)) {
            sizeOfActiveDeliveries.put(reservationStatus.name(),reservationRepository.findReservationsByStatus(reservationStatus).size());
        }

        return sizeOfActiveDeliveries;
    }

    @Override
    public void changeReservationStatus(long reservationId, String status) {
        Reservation reservation = reservationRepository.findById(reservationId).get();
        ReservationStatus reservationStatus = ReservationStatus.valueOf(status);

        if(reservationStatus.equals(ReservationStatus.CANCELED) || reservationStatus.equals(ReservationStatus.VISITED)){
            reservation.setActive(false);
        }

        reservation.setStatus(reservationStatus);
        reservationRepository.saveAndFlush(reservation);
    }
}
