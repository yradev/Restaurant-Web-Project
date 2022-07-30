package source.restaurant_web_project.configuration;

import org.springframework.stereotype.Component;
import source.restaurant_web_project.configuration.enums.RestaurantStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Component
public class RestaurantConfiguration {
    @Enumerated(EnumType.STRING)
    private RestaurantStatus status;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;

    public RestaurantConfiguration(){}

    public RestaurantStatus getStatus() {
        return status;
    }

    public void setStatus(RestaurantStatus status) {
        this.status = status;
    }

    public LocalDateTime getOpenTime() {
        return openTime;
    }

    public void setOpenTime(LocalDateTime openTime) {
        this.openTime = openTime;
    }

    public LocalDateTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(LocalDateTime closeTime) {
        this.closeTime = closeTime;
    }
}
