package source.restaurant_web_project.models.dto.category;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CategoryDTO {
    @NotNull
    @NotBlank
    @Size(min = 3,max = 15,message = "Category name must be between 3 and 15 chars!")
    private String name;

    @NotNull
    @NotBlank
    @Size(max = 30, message = "Category description must be max 30 chars!")
    private long position;

    public CategoryDTO(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }
}
