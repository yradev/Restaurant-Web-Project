package source.restaurant_web_project.model.dto.configuration;

import org.springframework.format.annotation.DateTimeFormat;
import source.restaurant_web_project.configuration.enums.DayOfWeeks;
import source.restaurant_web_project.configuration.enums.RestaurantStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;
import java.time.LocalTime;

public class RestaurantConfigurationDTO {
    @Size(min=3, max = 20,message = "Restaurant name must be between 3 and 20 chars!")
    private String name;
    @Size(max=50,message = "Restaurant description must be max 50 chars!")
    private String description;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime openTime;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime closeTime;

    @Enumerated(EnumType.STRING)
    private RestaurantStatus guestsStatus;
    private DayOfWeeks openDay;
    private DayOfWeeks closeDay;

    private String firmName;
    @Enumerated(EnumType.STRING)
    private RestaurantStatus deliveryStatus;
    private boolean configured;
    private String ownerNames;
    private String contactsPhoneNumber;

    private String location;

    public RestaurantStatus getGuestsStatus() {
        return guestsStatus;
    }

    public void setGuestsStatus(RestaurantStatus guestsStatus) {
        this.guestsStatus = guestsStatus;
    }

    public RestaurantConfigurationDTO(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalTime getOpenTime() {
        return openTime;
    }

    public void setOpenTime(LocalTime openTime) {
        this.openTime = openTime;
    }

    public LocalTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(LocalTime closeTime) {
        this.closeTime = closeTime;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public DayOfWeeks getOpenDay() {
        return openDay;
    }

    public void setOpenDay(DayOfWeeks openDay) {
        this.openDay = openDay;
    }

    public DayOfWeeks getCloseDay() {
        return closeDay;
    }

    public void setCloseDay(DayOfWeeks closeDay) {
        this.closeDay = closeDay;
    }

    public RestaurantStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(RestaurantStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isConfigured() {
        return configured;
    }

    public void setConfigured(boolean configured) {
        this.configured = configured;
    }

    public String getOwnerNames() {
        return ownerNames;
    }

    public void setOwnerNames(String ownerNames) {
        this.ownerNames = ownerNames;
    }

    public String getContactsPhoneNumber() {
        return contactsPhoneNumber;
    }

    public void setContactsPhoneNumber(String contactsPhoneNumber) {
        this.contactsPhoneNumber = contactsPhoneNumber;
    }
}
