package source.restaurant_web_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import source.restaurant_web_project.model.entity.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {
}
