package source.restaurant_web_project.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import source.restaurant_web_project.models.dto.ChangeStatusDTO;
import source.restaurant_web_project.models.dto.lunchmenu.LunchMenuAddDTO;
import source.restaurant_web_project.services.LunchMenuService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("lunch-menu")
public class LunchMenuController {
    private final LunchMenuService lunchMenuService;

    public LunchMenuController(LunchMenuService lunchMenuService) {
        this.lunchMenuService = lunchMenuService;
    }

    @GetMapping
    public ResponseEntity<?>getTodayLunchMenu(){
        return ResponseEntity.ok(lunchMenuService.getTodayLunchMenu());
    }

    @PreAuthorize("hasRole('ROLE_STAFF')")
    @GetMapping("old")
    public ResponseEntity<?>getOldMenus() {
        return ResponseEntity.ok(lunchMenuService.getOldLunchMenus());
    }

    @PreAuthorize("hasRole('ROLE_STAFF')")
    @PostMapping("add")
    public ResponseEntity<?>addTodayMenu(@RequestBody LunchMenuAddDTO lunchMenuAddDTO){
        return ResponseEntity.created(linkTo(methodOn(this.getClass())
                .getTodayLunchMenu())
                .toUri())
                .build();
    }

    @PreAuthorize("hasRole('ROLE_STAFF')")
    @PutMapping("status")
    public ResponseEntity<?>changeStatus(@RequestBody ChangeStatusDTO changeStatusDTO) {
        lunchMenuService.changeStatus(changeStatusDTO.getStatus());
        return ResponseEntity.ok().build();
    }

}
