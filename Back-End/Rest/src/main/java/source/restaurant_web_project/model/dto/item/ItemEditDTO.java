package source.restaurant_web_project.model.dto.item;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class ItemEditDTO {
    private String name;
    @Size(max = 30,message = "Description must be max 30 symbols")
    private String description;
    @Positive(message = "Price must be more than 0!")
    private BigDecimal price;
    private long position;
    private String currentCategory;
    private String category;
    private String currentItemName;
    private int currentViewPage;

    public ItemEditDTO() {
    }

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

    public String getCurrentCategory() {
        return currentCategory;
    }

    public void setCurrentCategory(String currentCategory) {
        this.currentCategory = currentCategory;
    }

    public String getCurrentItemName() {
        return currentItemName;
    }

    public void setCurrentItemName(String currentItemName) {
        this.currentItemName = currentItemName;
    }

    public int getCurrentViewPage() {
        return currentViewPage;
    }

    public void setCurrentViewPage(int currentViewPage) {
        this.currentViewPage = currentViewPage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
