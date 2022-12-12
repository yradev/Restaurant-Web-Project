package source.restaurant_web_project.models.dto.item;

import javax.validation.constraints.Size;

public class CategoryAddDTO {
    @Size(min = 3,max = 15,message = "Category name must be between 3 and 15 chars!")
    private String name;
    @Size(max = 30, message = "Category description must be max 30 chars!")
    private String description;

    public CategoryAddDTO(){}

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
