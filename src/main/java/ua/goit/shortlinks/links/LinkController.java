package ua.goit.shortlinks.links;

import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "LinkController")
@RequestMapping("/api/v1/links")
public class LinkController {
    private final LinkService linkService;

    @PostMapping
    public CreateLinkResponse create(Principal principal, @RequestBody CreateLinkRequest request) {
        return linkService.create(principal.getName(), request);
    }
    @GetMapping("/getUserLinks")
    public GetUserLinksResponse getUserLinks(Principal principal) {
        String username = principal.getName();
        return linkService.getUserLinks(username);
    }

    @PatchMapping
    public UpdateLinkResponse update(Principal principal, @RequestBody UpdateLinkRequest request) {//АПДЕЙТ
        return linkService.update(principal.getName(), request);
    }

    @DeleteMapping("/{shortLink}")
    public DeleteLinkResponse delete(Principal principal, @PathVariable String shortLink) {
        return linkService.delete(principal.getName(), shortLink);
    }
}