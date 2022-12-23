package source.restaurant_web_project.services.impl;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import source.restaurant_web_project.controllers.CategoryController;
import source.restaurant_web_project.controllers.ItemController;
import source.restaurant_web_project.errors.*;
import source.restaurant_web_project.models.dto.category.CategoriesViewDTO;
import source.restaurant_web_project.models.dto.category.CategoryViewDTO;
import source.restaurant_web_project.models.dto.category.CategoryDTO;
import source.restaurant_web_project.models.entity.Category;
import source.restaurant_web_project.repositories.CategoryRepository;
import source.restaurant_web_project.services.CategoryService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class CategoryServiceIMPL implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryServiceIMPL(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoriesViewDTO getCategories(long pageId) {
        long totalEntities = categoryRepository.count();
        if (totalEntities == 0) {
            throw new NoContentException("We dont have categories!");
        }

        long totalPages = Math.round(Math.ceil((double) totalEntities / 10));

        if (pageId <= 0 && pageId > totalPages) {
            throw new NotFoundException("We dont have this page!");
        }

        CategoriesViewDTO categories = new CategoriesViewDTO(pageId, totalPages == 0 ? 1 : totalPages, totalEntities);

        categories.getCategories().addAll(
                categoryRepository.findAll().stream()
                        .sorted(Comparator.comparingLong(Category::getPosition))
                        .filter(a -> a.getPosition() > pageId * 10 - 10)
                        .limit(10)
                        .map(a -> modelMapper.map(a, CategoryViewDTO.class))
                        .peek(a -> a.add(linkTo(methodOn(ItemController.class).findItemsByCategory(a.getName(), 1)).withRel("View items")))
                        .toList()
        );

        if (pageId > 1 && totalPages >= pageId) {
            categories.add(linkTo(methodOn(CategoryController.class).categoriesByPageId(categories.getCurrentPage() - 1)).withRel("Past page"));
        }

        if (totalPages > pageId) {
            categories.add(linkTo(methodOn(CategoryController.class).categoriesByPageId(categories.getCurrentPage() + 1)).withRel("Next page"));
        }
        return categories;
    }

    @Override
    public String addCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.findCategoryByName(categoryDTO.getName()) != null) {
            throw new ConflictException("We have category with this name!");
        }
        Category category = modelMapper.map(categoryDTO, Category.class);
        category.setPosition(categoryRepository.count() + 1);
        categoryRepository.save(category);
        return category.getName();
    }

    @Override
    public void editCategory(String categoryName, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findCategoryByName(categoryName);

        List<Category> categoryList = new ArrayList<>();

        if (category == null) {
            throw new NotFoundException("We dont have category with this name!");
        }

        if (category.getName().equals(categoryDTO.getName()) &&
                category.getPosition() == categoryDTO.getPosition()) {
            throw new NotModifiedException();
        }


        if (categoryDTO.getName() != null && !categoryDTO.getName().equals(categoryName)) {
            if (categoryRepository.findCategoryByName(categoryName) != null) {
                throw new ConflictException("We have user with this name!");
            }
        }

        if (categoryDTO.getPosition() > 0 && category.getPosition() != categoryDTO.getPosition()) {
            Category oldCategory = categoryRepository.findCategoryByPosition(categoryDTO.getPosition());
            oldCategory.setPosition(category.getPosition());
            category.setPosition(categoryDTO.getPosition());
            categoryList.add(oldCategory);
        }

        categoryList.add(category);

        modelMapper.getConfiguration().setPropertyCondition(Conditions.not(a -> a.getSource() == null || a.getSource().toString().equals("0")));
        modelMapper.map(categoryDTO, category);

        categoryRepository.saveAllAndFlush(categoryList);
    }

    @Override
    public void deleteCategory(String name) {
        Category category = categoryRepository.findCategoryByName(name);
        if (category == null) {
            throw new NotFoundException("We dont have category with this name!");
        }

        categoryRepository.delete(category);

        List<Category> categories = categoryRepository.findAll().stream()
                .filter(a -> a.getPosition() > category.getPosition())
                .peek(a -> a.setPosition(a.getPosition() - 1))
                .toList();

        categoryRepository.saveAllAndFlush(categories);
    }
}
