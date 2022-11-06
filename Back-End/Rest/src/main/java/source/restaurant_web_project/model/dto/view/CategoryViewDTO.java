package source.restaurant_web_project.model.dto.view;

import java.math.BigDecimal;
import java.util.List;

public class CategoryViewDTO {
    private String name;
    private String description;

    public CategoryViewDTO() {
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

}