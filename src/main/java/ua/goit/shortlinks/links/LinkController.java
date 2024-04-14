package ua.goit.shortlinks.links;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.goit.shortlinks.links.dto.create.CreateLinkRequest;
import ua.goit.shortlinks.links.dto.create.CreateLinkResponse;
import ua.goit.shortlinks.links.dto.delete.DeleteLinkResponse;
import ua.goit.shortlinks.links.dto.get.GetUserLinksResponse;
import ua.goit.shortlinks.links.dto.update.UpdateLinkRequest;
import ua.goit.shortlinks.links.dto.update.UpdateLinkResponse;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/links")
public class LinkController {
    private final LinkService linkService;

    @PostMapping
    public CreateLinkResponse create(Principal principal, @RequestBody CreateLinkRequest request) {
        CreateLinkRequest requestWithoutShortLink = new CreateLinkRequest();
        requestWithoutShortLink.setOriginalLink(request.getOriginalLink());
        return linkService.create(principal.getName(), requestWithoutShortLink);
    }

    @GetMapping ("/getUserLinks")
    public GetUserLinksResponse getUserLinks(Principal principal) {
        return linkService.getUserLinks(principal.getName());
    }

    @PatchMapping
    public UpdateLinkResponse update(Principal principal, @RequestBody UpdateLinkRequest request) {
        return linkService.update(principal.getName(), request);
    }

    @DeleteMapping
    public DeleteLinkResponse delete(Principal principal, @RequestParam(name = "id") long id) {
        return linkService.delete(principal.getName(), id);
    }
}
