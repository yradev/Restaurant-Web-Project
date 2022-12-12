package source.restaurant_web_project.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import source.restaurant_web_project.configurations.authentication.JwtTokenUtil;
import source.restaurant_web_project.models.dto.authentication.*;
import source.restaurant_web_project.models.entity.User;
import source.restaurant_web_project.services.AuthService;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtTokenUtil jwtUtil;
    private final AuthService authService;

    public AuthController(AuthenticationManager authManager, JwtTokenUtil jwtUtil, AuthService authService) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody @Valid UserLoginDTO request) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword()));

            String accessToken = jwtUtil.generateAccessToken(authentication.getName());

            return ResponseEntity.ok().body(accessToken);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        }
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody @Valid UserRegisterDTO userRegisterDTO) {
        try {
            User user = authService.register(userRegisterDTO);
            String accessToken = jwtUtil.generateAccessToken(user.getEmail());

            return ResponseEntity.ok().body(accessToken);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("reset-password/verification/send")
    public ResponseEntity<String> sendVerification(@RequestBody ChangePasswordVerificationSendDto changePasswordVerificationSendDto) {
        try {
            authService.sendVerifyMessage(changePasswordVerificationSendDto.getEmail(), changePasswordVerificationSendDto.getUrl());

            return ResponseEntity.ok().build();
        } catch (BadCredentialsException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping("reset-password/verification/verify")
    public ResponseEntity<String> verifyTooken(@RequestBody VerifyResetPasswordTokenDTO verifyResetPasswordTokenDTO) {
        return authService.verifyToken(verifyResetPasswordTokenDTO.getTooken(), verifyResetPasswordTokenDTO.getEmail())
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().body("Tooken doesnt exist!");
    }

    @PutMapping("reset-password/{email}")
    public ResponseEntity<String> resetPassword(@PathVariable String email,@RequestBody @Valid ResetPasswordDTO resetPasswordDTO) {
        try {
            authService.resetPassword(email, resetPasswordDTO.getPassword());
           return ResponseEntity.ok().build();
        } catch (BadCredentialsException exception) {
          return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}
