package ua.goit.shortlinks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

@ExtendWith(MockitoExtension.class)
class ApplicationTests {
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Test
	@Profile("dev")
	void contextLoadsForDev() {
	}

	@Test
	@Profile("prod")
	void contextLoadsForProd() {
	}

}*/
