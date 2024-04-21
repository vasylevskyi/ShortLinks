package ua.goit.shortlinks.links;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.goit.shortlinks.links.Link;
import ua.goit.shortlinks.links.LinkRepository;
import ua.goit.shortlinks.links.LinkService;
import ua.goit.shortlinks.links.dto.create.CreateLinkRequest;
import ua.goit.shortlinks.links.dto.create.CreateLinkResponse;
import ua.goit.shortlinks.links.dto.delete.DeleteLinkResponse;
import ua.goit.shortlinks.links.dto.get.GetUserLinksResponse;
import ua.goit.shortlinks.users.User;
import ua.goit.shortlinks.users.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class LinkServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private LinkRepository linkRepository;

    @InjectMocks
    private LinkService linkService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }



    @Test
    public void testCreateLink_Failed_MultipleLinks() {
        String links = "https://example.com,https://anotherexample.com";
        CreateLinkRequest request = new CreateLinkRequest();
        request.setOriginalLink(links);

        CreateLinkResponse response = linkService.create("testUser", request);

        assertTrue(response.getError() == CreateLinkResponse.Error.multipleLinksProvided);
    }

    @Test
    public void testCreateLink_Failed_InvalidLink() {
        String links = "https://www.pornsahub.com/";
        CreateLinkRequest request = new CreateLinkRequest();
        request.setOriginalLink(links);

        CreateLinkResponse response = linkService.create("testUser", request);

        assertTrue(response.getError() == CreateLinkResponse.Error.invalidLink);
    }

    @Test
    public void testDeleteLink_Failed_LinkDoesNotExist() {
        String username = "john.doe@gmail.com";
        String shortLink = "AcDfGhJk";

        lenient().when(linkRepository.findByShortLink(shortLink)).thenReturn(Optional.empty());

        DeleteLinkResponse response = linkService.delete(username, shortLink);

        assertEquals(DeleteLinkResponse.Error.linkDoesNotExist, response.getError());
    }
    @Test
    public void testDeleteLink_Failed_NotUserLink() {
        String username = "john.doe@gmail.com";
        String shortLink = "AcDfGhJk";
        String anotherUser = "johnTwo.doe@gmail.com";

        User currentUser = new User();
        currentUser.setUserId(username);

        User ownerUser = new User();
        ownerUser.setUserId(anotherUser);

        Link link = new Link();
        link.setOriginalLink("https://www.pornsahub.com/");
        link.setShortLink(shortLink);
        link.setUser(ownerUser);

        lenient().when(linkRepository.findByShortLink(shortLink)).thenReturn(Optional.of(link));

        DeleteLinkResponse response = linkService.delete(username, shortLink);

        assertEquals(DeleteLinkResponse.Error.linkDoesNotExist, response.getError());
        assertFalse(link.isDeleted());
    }
}
