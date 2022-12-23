package source.restaurant_web_project.services;

import source.restaurant_web_project.models.dto.category.CategoriesViewDTO;
import source.restaurant_web_project.models.dto.category.CategoryDTO;

public interface CategoryService {
    CategoriesViewDTO getCategories(long pageId);

    String addCategory(CategoryDTO categoryDTO);

    void editCategory(String categoryName, CategoryDTO categoryDTO);

    void deleteCategory(String name);
}
