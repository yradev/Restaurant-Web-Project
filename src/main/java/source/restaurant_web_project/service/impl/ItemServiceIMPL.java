package source.restaurant_web_project.service.impl;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import source.restaurant_web_project.model.dto.item.CategoryAddDTO;
import source.restaurant_web_project.model.dto.item.CategoryEditDTO;
import source.restaurant_web_project.model.dto.item.ItemAddDTO;
import source.restaurant_web_project.model.dto.item.ItemEditDTO;
import source.restaurant_web_project.model.dto.view.CategoryViewDTO;
import source.restaurant_web_project.model.dto.view.ItemViewDTO;
import source.restaurant_web_project.model.entity.Category;
import source.restaurant_web_project.model.entity.Item;
import source.restaurant_web_project.repository.CategoryRepository;
import source.restaurant_web_project.repository.ItemRepository;
import source.restaurant_web_project.service.ItemService;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ItemServiceIMPL implements ItemService {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public ItemServiceIMPL(ItemRepository itemRepository, CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration().setPropertyCondition(
                a->{
                    boolean result = true;

                    if(a.getSource().toString().length()<=0){
                        result = false;
                    }

                    if(a.getSource().toString().length()==1 && Long.parseLong(a.getSource().toString())==0){
                        result = false;
                    }

                    return result;
                });

    }


    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findCategory(String name) {
        return categoryRepository.findCategoryByName(name);
    }

    @Override
    public void addCategory(CategoryAddDTO categoryAddDTO) {
        Category category = modelMapper.map(categoryAddDTO,Category.class);
        category.setPosition(categoryRepository.count()); // Becouse we have Archive category
        categoryRepository.save(category);
    }

    @Override
    public void editCategory(CategoryEditDTO categoryEditDTO) {
        Category category = categoryRepository.findCategoryByName(categoryEditDTO.getCurrentName());

        if(categoryEditDTO.getPosition()>0 && category.getPosition()!= categoryEditDTO.getPosition()){
            long oldCategoryPositionID = category.getPosition();
            long newCategoryPositionID = categoryEditDTO.getPosition();

            Category category2 = categoryRepository.findCategoryByPosition(newCategoryPositionID);
            category2.setPosition(oldCategoryPositionID);
            categoryRepository.save(category2);
        }

        modelMapper.map(categoryEditDTO,category);
        categoryRepository.saveAndFlush(category);
    }

    @Override
    public List<Item> getItemsByCategory(String categoryName) {
        return itemRepository.findAll().stream().filter(item -> item.getCategory().getName().equals(categoryName)).collect(Collectors.toList());
    }

    @Override
    public void addItem(ItemAddDTO itemAddDTO) {
        long itemCount = itemRepository.count() - itemRepository.findItemsByCategory_Name("Archive").size();
        itemAddDTO.setPosition(itemCount+1);
        Item item = modelMapper.map(itemAddDTO,Item.class);
        item.setCategory(categoryRepository.findCategoryByName(itemAddDTO.getCategoryName()));
        itemRepository.save(item);
    }


    @Override
    public void deleteCategory(String name) {
        Category currentCategory = categoryRepository.findCategoryByName(name);
        long categoryLastPosition = currentCategory.getPosition() - 1;
        categoryRepository.delete(currentCategory);

        categoryRepository.saveAllAndFlush(categoryRepository.findAll().stream()
                .filter(category -> category.getPosition()>categoryLastPosition)
                .peek(a->a.setPosition(categoryLastPosition+1))
                .collect(Collectors.toList()));
    }

    @Override
    public void deleteItem(String itemName,String categoryName) {
        Item currentItem = itemRepository.findItemByName(itemName);

        long itemBeforePosition = currentItem.getPosition()-1;

        itemRepository.delete(currentItem);

        Category currentCategory = categoryRepository.findCategoryByName(categoryName);

        currentCategory.getItems().stream()
                .filter(item -> item.getPosition()>itemBeforePosition)
                .forEach(item -> item.setPosition(itemBeforePosition+1));

       categoryRepository.save(currentCategory);

    }

    @Override
    public long getCountOfItemsInCategory(String categoryName) {
        return categoryRepository.findCategoryByName(categoryName).getItems().size();
    }

    @Override
    public void editItem(ItemEditDTO itemEditDTO) {
        Item item = itemRepository.findItemByName(itemEditDTO.getCurrentItemName());
    }

    @Override
    public Item findItem(String name) {
        return itemRepository.findItemByName(name);
    }

    @Override
    public List<CategoryViewDTO> getCategoryNameAndDescription() {

        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        return categoryRepository.findAll().stream()
                .peek(category -> category.setName(category.getName()))
                .sorted(Comparator.comparingLong(Category::getPosition))
                .map(category -> modelMapper.map(category, CategoryViewDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemViewDTO> getItemsView(String categoryName) {


        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        return itemRepository.findItemsByCategory_Name(categoryName).stream()
                .sorted(Comparator.comparingLong(Item::getPosition))
                .map(item -> modelMapper.map(item,ItemViewDTO.class))
                .collect(Collectors.toList());

    }
}
