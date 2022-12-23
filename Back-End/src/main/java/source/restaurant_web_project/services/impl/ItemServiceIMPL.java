package source.restaurant_web_project.services.impl;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import source.restaurant_web_project.controllers.ItemController;
import source.restaurant_web_project.errors.BadRequestException;
import source.restaurant_web_project.errors.ConflictException;
import source.restaurant_web_project.errors.NotFoundException;
import source.restaurant_web_project.models.dto.item.*;
import source.restaurant_web_project.models.dto.item.ItemViewDTO;
import source.restaurant_web_project.models.entity.Category;
import source.restaurant_web_project.models.entity.Item;
import source.restaurant_web_project.repositories.CategoryRepository;
import source.restaurant_web_project.repositories.ItemRepository;
import source.restaurant_web_project.repositories.LunchMenuRepository;
import source.restaurant_web_project.services.ItemService;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    public String addItem(ItemControlDTO itemControlDTO) {
        Item item = modelMapper.map(itemControlDTO,Item.class);
        Category category = categoryRepository.findCategoryByName(itemControlDTO.getCategory());

        if(category == null){
            throw new NotFoundException("We dont have category with this name!");
        }

        if(itemRepository.findItemByName(itemControlDTO.getName())!=null){
            throw new ConflictException("We have item with this name!");
        }

        item.setCategory(category);
        itemRepository.save(item);

        return item.getName();
    }

    @Override
    public void deleteItem(String itemName) {
        Item currentItem = itemRepository.findItemByName(itemName);

        if(itemName==null){
            throw new NotFoundException("We dont have item with this name!");
        }

        itemRepository.delete(currentItem);

    }

    @Override
    public void editItem(String itemName, ItemControlDTO itemControlDTO) {
        Item currentItem = itemRepository.findItemByName(itemName);

        if(currentItem==null){
            throw new NotFoundException("We dont have item with this name!");
        }

        if(!itemName.equals(itemControlDTO.getName()) && itemControlDTO.getName()!=null){
            if(itemRepository.findItemByName(itemControlDTO.getName())!=null){
                throw new ConflictException("We have item with this name!");
            }
        }

        modelMapper.getConfiguration().setPropertyCondition(Conditions.and(a->a.getSource()!=null,a->!a.getSource().equals("") && !a.getSource().toString().equals("0")));

        modelMapper.map(itemControlDTO,currentItem);

        itemRepository.saveAndFlush(currentItem);
    }

    @Override
    public ItemViewDTO findItem(String name) {
        Item item = itemRepository.findItemByName(name);
        if(item==null){
            throw new NotFoundException("We dont have item with this name!");
        }
        return modelMapper.map(item,ItemViewDTO.class);
    }

    @Override
    public ItemsByCategoryViewDTO findItemsByCategory(long pageId, String categoryName) {
        Category category = categoryRepository.findCategoryByName(categoryName);
        if(category==null){
            throw new NotFoundException("We dont have category with this name!");
        }

        long totalEntities = category.getItems().size();
        long totalPages = totalEntities == 0 ? 1 : Math.round(Math.ceil((double) totalEntities / 10));


        ItemsByCategoryViewDTO itemsByCategoryViewDTO = new ItemsByCategoryViewDTO(pageId,totalPages,totalEntities,categoryName);

        List<ItemViewDTO>items = category.getItems().stream()
                .map(a->modelMapper.map(a,ItemViewDTO.class))
                .toList();

        itemsByCategoryViewDTO.setItems(items);

        if (pageId > 1 && totalPages >= pageId) {
            itemsByCategoryViewDTO.add(linkTo(methodOn(ItemController.class).findItemsByCategory(categoryName,pageId - 1)).withRel("Past pageId"));
        }

        if (totalPages > pageId) {
            itemsByCategoryViewDTO.add(linkTo(methodOn(ItemController.class).findItemsByCategory(categoryName,pageId + 1)).withRel("Next pageId"));
        }

        return itemsByCategoryViewDTO;
    }


}