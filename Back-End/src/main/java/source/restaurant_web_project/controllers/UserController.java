package source.restaurant_web_project.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import source.restaurant_web_project.models.dto.user.UserControlDTO;
import source.restaurant_web_project.models.dto.user.UserDataSendDTO;
import source.restaurant_web_project.models.dto.user.UserEditDTO;
import source.restaurant_web_project.services.UserService;

import java.security.Principal;
import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("user/get")
    public ResponseEntity<UserDataSendDTO>getUserData(Principal principal){
        return ResponseEntity.ok(userService.getUserData(principal.getName()));
    }

    @PutMapping("user/edit")
    public ResponseEntity<?>editUserData(@RequestBody UserEditDTO userEditDTO){
        try{
            userService.editUserData(userEditDTO);
            return ResponseEntity.ok().build();
        }catch (BadCredentialsException exc){
            return ResponseEntity.badRequest().body(exc.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @GetMapping("user/get/{email}")
    public ResponseEntity<UserDataSendDTO>getUserData(@PathVariable String email){
        return ResponseEntity.ok(userService.getUserData(email));
    }

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @GetMapping("roles/get")
    public ResponseEntity<?>getActiveRoles(){
        return ResponseEntity.ok(userService.getActiveRoles());
    }


    @PreAuthorize("hasRole('ROLE_OWNER')")
    @GetMapping("user/control/get/{email}")
    public ResponseEntity<?>getUserCoreSetings(@PathVariable String email){
        try{
            return ResponseEntity.ok(userService.getUserCoreSettings(email));
        }catch (BadCredentialsException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

    }
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PutMapping("user/control/edit/{email}")
    public ResponseEntity<?>editUserCore(@PathVariable String email, @RequestBody UserControlDTO userData){
        try{
            userService.changeUserCoreData(email,userData);
            return ResponseEntity.ok().build();
        }catch (BadCredentialsException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
