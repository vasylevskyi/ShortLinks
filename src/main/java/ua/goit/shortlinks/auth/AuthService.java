package ua.goit.shortlinks.auth;

import ua.goit.shortlinks.auth.dto.login.LoginRequest;
import ua.goit.shortlinks.auth.dto.login.LoginResponse;
import ua.goit.shortlinks.auth.dto.registration.RegistrationRequest;
import ua.goit.shortlinks.auth.dto.registration.RegistrationResponse;
import ua.goit.shortlinks.security.JwtUtil;
import ua.goit.shortlinks.users.User;
import ua.goit.shortlinks.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public RegistrationResponse register(RegistrationRequest request) {
        User existingUser = userService.findByUsername(request.getEmail());

        if (Objects.nonNull(existingUser)) {
            return RegistrationResponse.failedAndMessage(
                    RegistrationResponse.Error.userAlreadyExists,
                    "User already exists"
            );
        }

        Optional<RegistrationResponse> validationError = validateRegistrationFields(request);

        if (validationError.isPresent()) {
            return validationError.get();
        }

        userService.saveUser(User.builder()
                .userId(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .build());

        return RegistrationResponse.success();
    }

    public LoginResponse login(LoginRequest request) {
        Optional<LoginResponse.Error> validationError = validateLoginFields(request);

        if (validationError.isPresent()) {
            return LoginResponse.failed(validationError.get());
        }

        User user = userService.findByUsername(request.getEmail());

        if (Objects.isNull(user)) {
            return LoginResponse.failed(LoginResponse.Error.invalidEmail);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            return LoginResponse.failed(LoginResponse.Error.wrongPassword);
        }

        String authToken = jwtUtil.generateToken(request.getEmail());

        return LoginResponse.success(authToken);
    }

    private Optional<RegistrationResponse> validateRegistrationFields(RegistrationRequest request) {
        String passwordPattern = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        String emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

        if (Objects.isNull(request.getEmail()) || !Pattern.matches(emailPattern, request.getEmail())) {
            return Optional.of(RegistrationResponse.failedAndMessage(
                    RegistrationResponse.Error.invalidEmail,
                    "The email is either incorrectly formatted"
            ));
        }

        if (Objects.isNull(request.getPassword()) || !Pattern.matches(passwordPattern, request.getPassword())) {
            return Optional.of(RegistrationResponse.failedAndMessage(
                    RegistrationResponse.Error.invalidPassword,
                    "The password must contain a minimum of eight characters, including numbers, uppercase and lowercase letters."
            ));
        }
        return Optional.empty();
    }
    private Optional<LoginResponse.Error> validateLoginFields(LoginRequest request) {
        if (Objects.isNull(request.getEmail()) || request.getEmail().isEmpty()) {
            return Optional.of(LoginResponse.Error.invalidEmail);
        }

        if (Objects.isNull(request.getPassword()) || request.getPassword().isEmpty()) {
            return Optional.of(LoginResponse.Error.wrongPassword);
        }
        return Optional.empty();
    }
}