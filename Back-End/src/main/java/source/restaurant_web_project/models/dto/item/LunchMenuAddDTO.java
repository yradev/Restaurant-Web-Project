package source.restaurant_web_project.models.dto.item;

import source.restaurant_web_project.models.entity.enums.LunchMenuStatus;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public class LunchMenuAddDTO {
    private LocalDate dateNow;
    @Size(min = 3,max = 20,message = "Item name must be between 3 and 20 chars!")
    private String itemName1;
    @Size(min = 3,max = 20,message = "Item name must be between 3 and 20 chars!")
    private String itemName2;
    @Positive(message = "Price must be more than 0!")
    private BigDecimal totalPrice;
    private LunchMenuStatus status;

    public LunchMenuAddDTO(){}

    public LocalDate getDateNow() {
        return dateNow;
    }

    public void setDateNow(LocalDate dateNow) {
        this.dateNow = dateNow;
    }

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

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LunchMenuStatus getStatus() {
        return status;
    }

    public void setStatus(LunchMenuStatus status) {
        this.status = status;
    }
}
