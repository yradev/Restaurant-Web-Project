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
import source.restaurant_web_project.services.AuthService;

import javax.validation.Valid;

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
        authService.register(userRegisterDTO);
        String accessToken = jwtUtil.generateAccessToken(userRegisterDTO.getPassword());

        return ResponseEntity.ok().body(accessToken);
    }

    @PostMapping("reset-password/verification/send")
    public ResponseEntity<String> sendVerification(@RequestBody ChangePasswordVerificationSendDto changePasswordVerificationSendDto) {
        authService.sendVerifyMessage(changePasswordVerificationSendDto.getEmail(), changePasswordVerificationSendDto.getUrl());

        return ResponseEntity.ok().build();
    }

    @PutMapping("reset-password/{email}")
    public ResponseEntity<String> resetPassword(@PathVariable String email, @RequestBody @Valid ResetPasswordDTO resetPasswordDTO) {
        authService.resetPassword(resetPasswordDTO.getToken(), email, resetPasswordDTO.getPassword());
        return ResponseEntity.ok().build();
    }
}
