package ua.goit.shortlinks.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ua.goit.shortlinks.auth.dto.login.LoginRequest;
import ua.goit.shortlinks.auth.dto.login.LoginResponse;
import ua.goit.shortlinks.auth.dto.registration.RegistrationRequest;
import ua.goit.shortlinks.auth.dto.registration.RegistrationResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "AuthController")
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    @Operation(summary = "Register a new user", description = "Registers a new user with the system.")
    @PostMapping("/register")
    public RegistrationResponse register(@Valid @RequestBody RegistrationRequest request) {
        return authService.register(request);
    }
    @Operation(summary = "User login", description = "Logs in the user to the system.")
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}