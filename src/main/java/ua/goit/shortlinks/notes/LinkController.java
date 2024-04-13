package ua.goit.shortlinks.notes;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.goit.shortlinks.notes.dto.create.CreateLinkRequest;
import ua.goit.shortlinks.notes.dto.create.CreateLinkResponse;
import ua.goit.shortlinks.notes.dto.delete.DeleteLinkResponse;
import ua.goit.shortlinks.notes.dto.get.GetUserLinksResponse;
import ua.goit.shortlinks.notes.dto.update.UpdateLinkRequest;
import ua.goit.shortlinks.notes.dto.update.UpdateLinkResponse;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/links")
public class LinkController {
    private final LinkService linkService;

    @PostMapping
    public CreateLinkResponse create(Principal principal, @RequestBody CreateLinkRequest request) {
        return linkService.create(principal.getName(), request);
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
