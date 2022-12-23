package source.restaurant_web_project.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import source.restaurant_web_project.models.dto.user.UserControlDTO;
import source.restaurant_web_project.models.dto.user.UserDataViewDTO;
import source.restaurant_web_project.models.dto.user.UserEditDTO;
import source.restaurant_web_project.services.UserService;

import java.security.Principal;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<UserDataViewDTO> getUserData(Principal principal) {
        return ResponseEntity.ok(userService.getUserData(principal.getName()));
    }

    @PutMapping()
    public ResponseEntity<?> editUserData(@RequestBody UserEditDTO userEditDTO) {
        userService.editUserData(userEditDTO);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @GetMapping("{email}")
    public ResponseEntity<UserDataViewDTO> getUserData(@PathVariable String email) {
        UserDataViewDTO userDataViewDTO = userService.getUserData(email);
        userDataViewDTO.add(linkTo(methodOn(UserController.class).getUserCoreSetings(email)).withRel("User control"));
        return ResponseEntity.ok(userDataViewDTO);
    }

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @GetMapping("roles")
    public ResponseEntity<?> getActiveRoles() {
        return ResponseEntity.ok(userService.getActiveRoles());
    }


    @PreAuthorize("hasRole('ROLE_OWNER')")
    @GetMapping("control/{email}")
    public ResponseEntity<?> getUserCoreSetings(@PathVariable String email) {
        UserControlDTO userControlDTO = userService.getUserCoreSettings(email);
        userControlDTO.add(linkTo(methodOn(this.getClass()).getUserData(email)).withRel("User data"));
        return ResponseEntity.ok(userControlDTO);

    }

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PutMapping("user/control/{email}")
    public ResponseEntity<?> editUserCore(@PathVariable String email, @RequestBody UserControlDTO userData) {
        userService.changeUserCoreData(email, userData);
        return ResponseEntity.ok().build();
    }
}
