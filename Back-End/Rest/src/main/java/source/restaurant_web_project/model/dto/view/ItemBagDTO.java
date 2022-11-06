package source.restaurant_web_project.model.dto.view;

import java.math.BigDecimal;

public class ItemBagDTO {
    private String name;
    private long countInBag;
    private BigDecimal price;
    private BigDecimal totalPrice;

    public ItemBagDTO(){}

    public ItemBagDTO(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCountInBag() {
        return countInBag;
    }

    public void setCountInBag(long countInBag) {
        this.countInBag = countInBag;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
