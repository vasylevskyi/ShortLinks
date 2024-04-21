package ua.goit.shortlinks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.goit.shortlinks.auth.AuthService;
import ua.goit.shortlinks.auth.dto.login.LoginRequest;
import ua.goit.shortlinks.auth.dto.login.LoginResponse;
import ua.goit.shortlinks.auth.dto.registration.RegistrationRequest;
import ua.goit.shortlinks.auth.dto.registration.RegistrationResponse;
import ua.goit.shortlinks.security.JwtUtil;
import ua.goit.shortlinks.users.User;
import ua.goit.shortlinks.users.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private AuthService authService;
    private final String email = "test@example.com";
    private final String password = "Password123";
    private final String token = "fake-jwt-token";

    @BeforeEach
    public void setup() {
        Mockito.lenient().when(passwordEncoder.encode(anyString())).thenReturn("hashed-password");
        Mockito.lenient().when(jwtUtil.generateToken(anyString())).thenReturn(token);
    }

    @Test
    public void testRegisterSuccess() {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail(email);
        request.setPassword(password);

        when(userService.findByUsername(email)).thenReturn(null);

        RegistrationResponse response = authService.register(request);

        assertNotNull(response, "Response should not be null");
        assertEquals(RegistrationResponse.Error.ok, response.getError(), "Expected successful registration");
        assertNull(response.getMessage(), "No error message should be present for successful registration");

        verify(userService, times(1)).saveUser(any(User.class));
    }

    @Test
    public void testRegisterUserAlreadyExists() {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail(email);
        request.setPassword(password);

        when(userService.findByUsername(email)).thenReturn(new User());

        RegistrationResponse response = authService.register(request);

        assertNotNull(response, "Response should not be null");
        assertEquals(RegistrationResponse.Error.userAlreadyExists, response.getError(), "Expected 'user already exists' error");
        assertEquals("User already exists", response.getMessage(), "Correct error message expected");

        verify(userService, times(0)).saveUser(any(User.class));
    }

    @Test
    public void testRegisterInvalidEmail() {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("invalid-email");
        request.setPassword("Password123");

        RegistrationResponse response = authService.register(request);

        assertNotNull(response, "Response should not be null");
        assertEquals(RegistrationResponse.Error.invalidEmail, response.getError(), "Expected 'invalidEmail' error");
        assertEquals("The email is either incorrectly formatted", response.getMessage(), "Expected appropriate error message");
    }
    @Test
    public void testRegisterInvalidPassword() {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("test@example.com");
        request.setPassword("short");

        RegistrationResponse response = authService.register(request);

        assertNotNull(response, "Response should not be empty");
        assertEquals(RegistrationResponse.Error.invalidPassword, response.getError(), "Expected error invalidPassword");
        assertEquals("The password must contain a minimum of eight characters, including numbers, uppercase and lowercase letters.", response.getMessage(), "Password error message");
    }

    @Test
    public void testLoginSuccess() {
        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);

        User user = new User();
        user.setPasswordHash("hashed-password");

        when(userService.findByUsername(email)).thenReturn(user);
        when(passwordEncoder.matches(password, user.getPasswordHash())).thenReturn(true);

        LoginResponse response = authService.login(request);

        assertNotNull(response, "Response should not be null");
        assertEquals(LoginResponse.Error.ok, response.getError(), "Expected successful login");
        assertEquals(token, response.getAuthToken(), "Expected correct JWT token");

        verify(userService, times(1)).findByUsername(email);
    }

    @Test
    public void testLoginInvalidEmail() {
        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);

        when(userService.findByUsername(email)).thenReturn(null);

        LoginResponse response = authService.login(request);

        assertNotNull(response, "Response should not be null");
        assertEquals(LoginResponse.Error.invalidEmail, response.getError(), "Expected 'invalid email' error");

        verify(userService, times(1)).findByUsername(email);
    }

    @Test
    public void testLoginWrongPassword() {
        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);

        User user = new User();
        user.setPasswordHash("hashed-password");

        when(userService.findByUsername(email)).thenReturn(user);
        when(passwordEncoder.matches(password, user.getPasswordHash())).thenReturn(false);

        LoginResponse response = authService.login(request);

        assertNotNull(response, "Response should not be null");
        assertEquals(LoginResponse.Error.wrongPassword, response.getError(), "Expected 'wrong password' error");

        verify(userService, times(1)).findByUsername(email);
    }

    @Test
    public void testLoginEmptyEmailOrPassword() {
        LoginRequest request1 = new LoginRequest();
        request1.setEmail("");
        request1.setPassword(password);

        LoginResponse response1 = authService.login(request1);

        assertNotNull(response1, "Response should not be null");
        assertEquals(LoginResponse.Error.invalidEmail, response1.getError(), "Expected 'invalid email' error");

         LoginRequest request2 = new LoginRequest();
        request2.setEmail(email);
        request2.setPassword("");

        LoginResponse response2 = authService.login(request2);

        assertNotNull(response2, "Response should not be null");
        assertEquals(LoginResponse.Error.wrongPassword, response2.getError(), "Expected 'wrong password' error");
    }
}
