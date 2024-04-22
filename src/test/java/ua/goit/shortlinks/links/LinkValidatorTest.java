package ua.goit.shortlinks.links;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LinkValidatorTest {

    @Test
    public void testIsLinkValid() {
        boolean isValid = LinkValidator.isLinkValid("http://www.google.com");

        assertTrue(isValid);
    }
}
