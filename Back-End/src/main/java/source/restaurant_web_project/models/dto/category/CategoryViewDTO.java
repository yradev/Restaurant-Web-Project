package source.restaurant_web_project.models.dto.category;

import org.springframework.hateoas.RepresentationModel;

public class CategoryViewDTO extends RepresentationModel<CategoryViewDTO> {
    private String name;
    private int position;

    public CategoryViewDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
