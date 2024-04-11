package ua.goit.shortlinks.auth;

import ua.goit.shortlinks.auth.dto.login.LoginRequest;
import ua.goit.shortlinks.auth.dto.login.LoginResponse;
import ua.goit.shortlinks.auth.dto.registration.RegistrationRequest;
import ua.goit.shortlinks.auth.dto.registration.RegistrationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public RegistrationResponse register(@RequestBody RegistrationRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
