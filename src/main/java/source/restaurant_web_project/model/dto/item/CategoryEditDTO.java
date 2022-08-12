package source.restaurant_web_project.model.dto.item;

import javax.validation.constraints.Size;

public class CategoryEditDTO {
    private String currentName;
    @Size(min = 3,max = 10,message = "Category name must be between 3 and 10 chars!")
    private String name;
    @Size(max = 30, message = "Category description must be max 30 chars!")
    private String description;
    private long position;

    public CategoryEditDTO(){}

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

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public String getCurrentName() {
        return currentName;
    }

    public void setCurrentName(String currentName) {
        this.currentName = currentName;
    }

}
