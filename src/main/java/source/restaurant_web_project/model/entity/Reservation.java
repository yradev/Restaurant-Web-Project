package source.restaurant_web_project.model.entity;

import source.restaurant_web_project.model.entity.enums.ReservationStatus;
import source.restaurant_web_project.model.entity.superClass.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation extends BaseEntity {
    private LocalDateTime reservationFor;
    private int countOfPersons;
    private ReservationStatus status;

    @ManyToOne(targetEntity = User.class)
    private User user;

    public Reservation(){}

    public LocalDateTime getReservationFor() {
        return reservationFor;
    }

    public void setReservationFor(LocalDateTime reservationFor) {
        this.reservationFor = reservationFor;
    }

    public int getCountOfPersons() {
        return countOfPersons;
    }

    public void setCountOfPersons(int countOfPersons) {
        this.countOfPersons = countOfPersons;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
