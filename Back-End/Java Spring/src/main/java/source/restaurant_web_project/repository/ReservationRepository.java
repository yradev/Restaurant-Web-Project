package source.restaurant_web_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import source.restaurant_web_project.model.entity.Reservation;
import source.restaurant_web_project.model.entity.enums.ReservationStatus;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    List<Reservation> findReservationsByUser_Username(String username);
    List<Reservation> findReservationsByStatus(ReservationStatus reservationStatus);
}
