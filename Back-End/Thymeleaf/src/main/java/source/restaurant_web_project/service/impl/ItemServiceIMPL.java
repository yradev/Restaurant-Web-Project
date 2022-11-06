package source.restaurant_web_project.service.impl;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import source.restaurant_web_project.model.dto.item.*;
import source.restaurant_web_project.model.dto.view.CategoryViewDTO;
import source.restaurant_web_project.model.dto.view.ItemViewDTO;
import source.restaurant_web_project.model.dto.view.LunchMenuViewDTO;
import source.restaurant_web_project.model.entity.Category;
import source.restaurant_web_project.model.entity.Item;
import source.restaurant_web_project.model.entity.LunchMenu;
import source.restaurant_web_project.repository.CategoryRepository;
import source.restaurant_web_project.repository.ItemRepository;
import source.restaurant_web_project.repository.LunchMenuRepository;
import source.restaurant_web_project.service.ItemService;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceIMPL implements ItemService {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final LunchMenuRepository lunchMenuRepository;

    public ItemServiceIMPL(ItemRepository itemRepository, CategoryRepository categoryRepository, ModelMapper modelMapper, LunchMenuRepository lunchMenuRepository) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.lunchMenuRepository = lunchMenuRepository;
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
        category.setPosition(categoryRepository.count()+1 );
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

        modelMapper.getConfiguration().setPropertyCondition(Conditions.and(a->a.getSource()!=null,a->!a.getSource().equals("") && !a.getSource().toString().equals("0")));
        modelMapper.map(categoryEditDTO,category);
        categoryRepository.saveAndFlush(category);
    }

    @Override
    public List<Item> getItemsByCategory(String categoryName) {
        return itemRepository.findAll().stream().filter(item -> !item.getName().equals("Lunch menu") && item.getCategory().getName().equals(categoryName)).collect(Collectors.toList());
    }

    @Override
    public void addItem(ItemAddDTO itemAddDTO) {
        long itemCount = itemRepository.findItemsByCategory_Name(itemAddDTO.getCategoryName()).size();
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
    public void editItem(ItemEditDTO itemEditDTO) {
        boolean isCategoryChanged = false;
        Item currentItem = itemRepository.findItemByName(itemEditDTO.getCurrentItemName());

        if(itemEditDTO.getPosition()!=0){
            Item oldPositionItem = itemRepository.findItemsByCategory_Name(itemEditDTO.getCurrentCategory()).stream()
                    .filter(item->item.getPosition()==itemEditDTO.getPosition())
                    .findFirst()
                    .orElse(null);

            oldPositionItem.setPosition(currentItem.getPosition());
            itemRepository.saveAndFlush(oldPositionItem);
        }

        modelMapper.getConfiguration().setPropertyCondition(Conditions.and(a->a.getSource()!=null,a->!a.getSource().equals("") && !a.getSource().toString().equals("0")));
        modelMapper.map(itemEditDTO,currentItem);
        if(!itemEditDTO.getCategory().equals("0")){
            currentItem.setCategory(categoryRepository.findCategoryByName(itemEditDTO.getCategory()));
            currentItem.setPosition(categoryRepository.findCategoryByName(itemEditDTO.getCategory()).getItems().size()+1);
            isCategoryChanged=true;
        }
        itemRepository.saveAndFlush(currentItem);

        if(isCategoryChanged){
            long oldPosition = itemEditDTO.getPosition()-1;
           Category oldCategory = categoryRepository.findCategoryByName(itemEditDTO.getCurrentCategory());
           oldCategory.getItems().stream()
                   .filter(item->item.getPosition()>oldPosition)
                   .forEach(item->item.setPosition(item.getPosition()-1));
           categoryRepository.saveAndFlush(oldCategory);
        }
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
