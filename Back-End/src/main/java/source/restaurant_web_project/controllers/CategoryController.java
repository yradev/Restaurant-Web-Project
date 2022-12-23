package source.restaurant_web_project.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import source.restaurant_web_project.errors.BadRequestException;
import source.restaurant_web_project.models.dto.category.CategoryDTO;
import source.restaurant_web_project.services.CategoryService;
import source.restaurant_web_project.services.UserService;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping(value = "categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<?> categories() {
        return ResponseEntity.ok(categoryService.getCategories(1));
    }

    @GetMapping("{categoryName}")
    public ResponseEntity<?>getCategory(@PathVariable String categoryName){
        return ResponseEntity.ok().build();
    }

    @GetMapping("page/{pageId}")
    public ResponseEntity<?> categoriesByPageId(@PathVariable long pageId) {
        return ResponseEntity.ok(categoryService.getCategories(pageId));
    }

    @PostMapping("add")
    public ResponseEntity<?> categoryAdd(@RequestBody @Valid CategoryDTO categoryDTO) {
        String categoryName = categoryService.addCategory(categoryDTO);
        return ResponseEntity.created(linkTo(methodOn(this.getClass()).getCategory(categoryName)).toUri()).build();
    }

    @PutMapping("edit/{categoryName}")
    public ResponseEntity<?> categoryEdit(@RequestBody @Valid CategoryDTO categoryDTO, @PathVariable String categoryName) {
        categoryService.editCategory(categoryName, categoryDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("delete/{categoryName}")
    public ResponseEntity<?> categoryDelete(@PathVariable String categoryName) {
        categoryService.deleteCategory(categoryName);
        return ResponseEntity.ok().build();
    }
}
