package source.restaurant_web_project.model.dto;

import org.springframework.format.annotation.DateTimeFormat;
import source.restaurant_web_project.model.entity.enums.ReservationStatus;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

public class NewReservationDTO {
    @Future
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime reservationFor;
    private int countOfPersons;

    public NewReservationDTO(){}

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
}
