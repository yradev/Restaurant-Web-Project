package source.restaurant_web_project.models.dto.item;

import source.restaurant_web_project.models.dto.superClass.ListViewDTO;

import java.util.List;

public class ItemsByCategoryViewDTO extends ListViewDTO {
    private String categoryName;
    private List<ItemViewDTO> items;

    public ItemsByCategoryViewDTO(long currentPage, long totalPages, long totalEntities, String categoryName) {
        super(currentPage, totalPages, totalEntities);
        this.categoryName = categoryName;
    }

    public List<ItemViewDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemViewDTO> items) {
        this.items = items;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
