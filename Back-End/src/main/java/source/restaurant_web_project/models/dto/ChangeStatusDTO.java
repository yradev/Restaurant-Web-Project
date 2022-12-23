package source.restaurant_web_project.models.dto;

import source.restaurant_web_project.models.entity.enums.DeliveryStatus;

public class ChangeStatusDTO {
    private String status;

    public ChangeStatusDTO(){};

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
