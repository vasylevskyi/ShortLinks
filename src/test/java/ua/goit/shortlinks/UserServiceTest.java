package ua.goit.shortlinks;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import ua.goit.shortlinks.users.User;
import ua.goit.shortlinks.users.UserRepository;
import ua.goit.shortlinks.users.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testFindByUsername_UserExists() {
        User mockUser = new User("test_user", "test_password", null);
        Optional<User> optionalUser = Optional.of(mockUser);

        when(userRepository.findById("test_user")).thenReturn(optionalUser);

        User result = userService.findByUsername("test_user");

        assertEquals(mockUser, result);
    }

    @Test
    public void testSaveUser() {
        User mockUser = new User("test_user", "test_password", null);

        userService.saveUser(mockUser);

        verify(userRepository, times(1)).save(mockUser);
    }
}
