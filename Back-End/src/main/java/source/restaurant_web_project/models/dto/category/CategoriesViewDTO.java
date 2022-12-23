package source.restaurant_web_project.models.dto.category;

import org.springframework.hateoas.RepresentationModel;
import source.restaurant_web_project.models.dto.superClass.ListViewDTO;

import java.util.ArrayList;
import java.util.List;

public class CategoriesViewDTO extends ListViewDTO {
    private List<CategoryViewDTO> categories;

    public CategoriesViewDTO(long currentPage, long totalPages, long totalEntities) {
        super(currentPage, totalPages, totalEntities);
    }

    public List<CategoryViewDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryViewDTO> categories) {
        this.categories = categories;
    }
}
