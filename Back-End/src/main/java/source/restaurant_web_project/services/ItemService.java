package source.restaurant_web_project.services;

import source.restaurant_web_project.models.dto.category.CategoryViewDTO;
import source.restaurant_web_project.models.dto.item.*;
import source.restaurant_web_project.models.dto.item.ItemViewDTO;
import source.restaurant_web_project.models.entity.Item;

import java.util.List;

public interface ItemService {
    String addItem(ItemControlDTO itemAddDTO);

    void deleteItem(String itemName);

    void editItem(String itemName, ItemControlDTO itemControlDTO);

    ItemViewDTO findItem(String name);

    ItemsByCategoryViewDTO findItemsByCategory(long page,String categoryName);
}
