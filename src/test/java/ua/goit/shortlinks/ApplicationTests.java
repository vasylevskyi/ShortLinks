package ua.goit.shortlinks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;

@ExtendWith(MockitoExtension.class)
public class ApplicationTests {

    @Test
    @Profile("dev")
    void contextLoadsForDev() {
    }

    @Test
    @Profile("prod")
    void contextLoadsForProd() {
    }
}
