package ua.goit.shortlinks.links;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RedirectControllerTest {

    private final LinkService linkService = mock(LinkService.class);
    private final RedirectController redirectController = new RedirectController(linkService);

    @Test
    public void testRedirect() {
        Link link = new Link();
        link.setOriginalLink("http://original.link");
        link.setValidTo(LocalDateTime.now().plusDays(1));
        when(linkService.findByShortLink("shortLink")).thenReturn(link);

        ResponseEntity<Void> response = redirectController.redirect("shortLink");

        assertEquals(HttpStatus.MOVED_PERMANENTLY, response.getStatusCode());
        verify(linkService, times(1)).save(any(Link.class));
    }
}

