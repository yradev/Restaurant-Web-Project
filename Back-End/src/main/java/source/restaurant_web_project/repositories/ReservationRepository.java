package source.restaurant_web_project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import source.restaurant_web_project.models.entity.Reservation;
import source.restaurant_web_project.models.entity.enums.ReservationStatus;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    List<Reservation> findReservationsByUser_Email(String username);
    List<Reservation> findReservationsByStatus(ReservationStatus reservationStatus);
}
