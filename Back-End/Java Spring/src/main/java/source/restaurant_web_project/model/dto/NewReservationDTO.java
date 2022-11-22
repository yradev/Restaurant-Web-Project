package source.restaurant_web_project.model.dto;

import org.springframework.format.annotation.DateTimeFormat;
import source.restaurant_web_project.model.entity.enums.ReservationStatus;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class NewReservationDTO {
    @FutureOrPresent(message = "Reservation cant be in past!")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime reservationFor;
    private int countOfPersons;
    private String phoneNumber;


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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
