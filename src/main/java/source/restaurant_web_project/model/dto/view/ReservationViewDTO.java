package source.restaurant_web_project.model.dto.view;

import source.restaurant_web_project.model.entity.enums.ReservationStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReservationViewDTO {
    private long id;
    private String reservationFor;
    private int countOfPersons;
    private ReservationStatus status;
    private String userUsername;
    private boolean active;
    private String phoneNumber;

    public ReservationViewDTO(){}

    public String getReservationFor() {
        return reservationFor;
    }

    public void setReservationFor(LocalDateTime reservationFor) {

        this.reservationFor = reservationFor.format(DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm"));
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

    public String getUserUsername() {
        return userUsername;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

