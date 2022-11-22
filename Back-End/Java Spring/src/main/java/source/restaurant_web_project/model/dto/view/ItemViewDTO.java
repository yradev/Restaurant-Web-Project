package source.restaurant_web_project.model.dto.view;

import java.math.BigDecimal;

public class ItemViewDTO {
    private String name;
    private String description;
    private BigDecimal price;
    private long countInBag;

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public long getCountInBag() {
        return countInBag;
    }

    public void setCountInBag(long countInBag) {
        this.countInBag = countInBag;
    }
}
