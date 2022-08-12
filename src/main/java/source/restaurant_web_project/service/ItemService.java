package source.restaurant_web_project.service;

import source.restaurant_web_project.model.dto.item.CategoryAddDTO;
import source.restaurant_web_project.model.dto.item.CategoryEditDTO;
import source.restaurant_web_project.model.dto.item.ItemAddDTO;
import source.restaurant_web_project.model.dto.item.ItemEditDTO;
import source.restaurant_web_project.model.dto.view.CategoryViewDTO;
import source.restaurant_web_project.model.dto.view.ItemViewDTO;
import source.restaurant_web_project.model.entity.Category;
import source.restaurant_web_project.model.entity.Item;

import java.util.List;

public interface ItemService {
    List<Category> getCategories();

    Category findCategory(String name);

    void addCategory(CategoryAddDTO categoryAddDTO);

    void editCategory(CategoryEditDTO categoryEditDTO);

    void deleteCategory(String name);

    List<Item> getItemsByCategory(String categoryName);

    void addItem(ItemAddDTO itemAddDTO);

    void deleteItem(String itemName, String categoryName);

    long getCountOfItemsInCategory(String categoryName);

    void editItem(ItemEditDTO itemEditDTO);

    Item findItem(String name);

    List<CategoryViewDTO> getCategoryNameAndDescription();

    List<ItemViewDTO> getItemsView(String categoryName);
}
