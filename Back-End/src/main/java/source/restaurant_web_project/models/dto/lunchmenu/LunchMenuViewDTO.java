package source.restaurant_web_project.models.dto.lunchmenu;

import source.restaurant_web_project.models.entity.enums.LunchMenuStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LunchMenuViewDTO {
    private LocalDate dateNow;
    private String itemName1;
    private String itemName2;
    private BigDecimal price;
    private LunchMenuStatus status;

    public LunchMenuViewDTO(){}

    public String getItemName1() {
        return itemName1;
    }

    public void setItemName1(String itemName1) {
        this.itemName1 = itemName1;
    }

    public String getItemName2() {
        return itemName2;
    }

    public void setItemName2(String itemName2) {
        this.itemName2 = itemName2;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getDateNow() {
        return dateNow;
    }

    public void setDateNow(LocalDate dateNow) {
        this.dateNow = dateNow;
    }

    public LunchMenuStatus getStatus() {
        return status;
    }

    public void setStatus(LunchMenuStatus status) {
        this.status = status;
    }
}
