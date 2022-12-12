package source.restaurant_web_project.models.dto.item;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class ItemAddDTO {
    @Size(min = 3, max = 20, message = "Item name must be between 3 and 20 chars!")
    private String name;
    @Size(max = 30,message = "Description must be max 30 symbols")
    private String description;

    @Positive(message = "Price must be more than 0!")
    @NotNull(message = "Price must be more than 0")
    private BigDecimal price;

    private long position;
    private String categoryName;

    public ItemAddDTO(){};

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

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
