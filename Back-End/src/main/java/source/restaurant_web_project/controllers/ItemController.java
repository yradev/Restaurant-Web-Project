package source.restaurant_web_project.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import source.restaurant_web_project.errors.BadRequestException;
import source.restaurant_web_project.models.dto.item.ItemControlDTO;
import source.restaurant_web_project.models.dto.item.ItemViewDTO;
import source.restaurant_web_project.models.dto.item.ItemsByCategoryViewDTO;
import source.restaurant_web_project.services.ItemService;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("add")
    public ResponseEntity<?> addItem(@RequestBody @Valid ItemControlDTO itemControlDTO) {
        String itemName =  itemService.addItem(itemControlDTO);
        return ResponseEntity.created(linkTo(methodOn(this.getClass()).findItem(itemName)).toUri()).build();
    }

    @DeleteMapping("delete/{itemName}")
    public ResponseEntity<?> deleteItem(@PathVariable String itemName) {
        itemService.deleteItem(itemName);
        return ResponseEntity.ok().build();
    }

    @PutMapping("edit/{itemName}")
    public ResponseEntity<?> editItem(@PathVariable String itemName, @RequestBody @Valid ItemControlDTO itemControlDTO) {
        itemService.editItem(itemName, itemControlDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{itemName}")
    public ResponseEntity<?> findItem(@PathVariable String itemName) {
        return ResponseEntity.ok(itemService.findItem(itemName));
    }

    @GetMapping("category/{categoryName}/page/{pageId}")
    public ResponseEntity<?> findItemsByCategory(@PathVariable String categoryName, @PathVariable long pageId) {
        return ResponseEntity.ok(itemService.findItemsByCategory(pageId, categoryName));
    }
}
