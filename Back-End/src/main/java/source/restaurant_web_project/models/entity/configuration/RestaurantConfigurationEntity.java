package source.restaurant_web_project.models.entity.configuration;

import source.restaurant_web_project.configurations.enums.DayOfWeeks;
import source.restaurant_web_project.configurations.enums.RestaurantStatus;
import source.restaurant_web_project.models.entity.superClass.BaseEntity;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "core")
public class RestaurantConfigurationEntity extends BaseEntity {
    private String name;
    @Column(columnDefinition = "text")
    private String description;
    @Enumerated(EnumType.STRING)
    private RestaurantStatus guestsStatus;
    private DayOfWeeks openDay;
    private LocalTime openTime;
    private DayOfWeeks closeDay;
    private LocalTime closeTime;
    private String firmName;
    @Enumerated(EnumType.STRING)
    private RestaurantStatus deliveryStatus;
    @Column(columnDefinition = "text")
    private String location;
    private boolean configured;
    private String ownerNames;
    private String contactsPhoneNumber;

    public RestaurantConfigurationEntity(){}

    public RestaurantStatus getGuestsStatus() {
        return guestsStatus;
    }

    public void setGuestsStatus(RestaurantStatus status) {
        this.guestsStatus = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public DayOfWeeks getCloseDay() {
        return closeDay;
    }

    public void setCloseDay(DayOfWeeks closeDay) {
        this.closeDay = closeDay;
    }

    public DayOfWeeks getOpenDay() {
        return openDay;
    }

    public void setOpenDay(DayOfWeeks openDay) {
        this.openDay = openDay;
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

    public String getContactsPhoneNumber() {
        return contactsPhoneNumber;
    }

    public void setContactsPhoneNumber(String contactsPhoneNumber) {
        this.contactsPhoneNumber = contactsPhoneNumber;
    }

    public String getOwnerNames() {
        return ownerNames;
    }

    public void setOwnerNames(String ownerNames) {
        this.ownerNames = ownerNames;
    }
}
