package source.restaurant_web_project.models.entity;

import source.restaurant_web_project.models.entity.enums.LunchMenuStatus;
import source.restaurant_web_project.models.entity.superClass.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
public class LunchMenu extends BaseEntity {
    private LocalDate dateNow;
    private String itemName1;
    private String itemName2;
    private BigDecimal totalPrice;
    private LunchMenuStatus status;

    public LunchMenu(){}

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
