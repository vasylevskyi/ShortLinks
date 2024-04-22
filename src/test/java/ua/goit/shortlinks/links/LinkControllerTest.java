package ua.goit.shortlinks.links;

import org.junit.jupiter.api.Test;
import ua.goit.shortlinks.links.dto.create.CreateLinkRequest;
import ua.goit.shortlinks.links.dto.create.CreateLinkResponse;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class LinkControllerTest {

    private final LinkService linkService = mock(LinkService.class);
    private final LinkController linkController = new LinkController(linkService);
    private final Principal principal = mock(Principal.class);

    @Test
    public void testCreate() {
        CreateLinkRequest request = new CreateLinkRequest();
        request.setOriginalLink("http://original.link");
        when(principal.getName()).thenReturn("username");
        CreateLinkResponse mockResponse = CreateLinkResponse.success(
                "http://short.link",
                "http://original.link"
        );
        when(linkService.create(anyString(), any(CreateLinkRequest.class))).thenReturn(mockResponse);

        CreateLinkResponse response = linkController.create(principal, request);

        assertEquals("http://original.link", response.getOriginalLink());
    }
}
