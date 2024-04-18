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
        return linkService.create(principal.getName(), request);
    }

    @GetMapping ("/getUserLinks") // 18 04 Надо попробовать сделать просто @GetMapping БЕЗ СКОБОК
    public GetUserLinksResponse getUserLinks(Principal principal) {
        return linkService.getUserLinks(principal.getName());
    }

    @PatchMapping// 18 04 Надо попробовать сделать @PatchMapping("/{shortLink}")
    public UpdateLinkResponse update(Principal principal, @RequestBody UpdateLinkRequest request) {//АПДЕЙТ
        return linkService.update(principal.getName(), request);
    }

//    @DeleteMapping// 18 04 Надо попробовать сделать
    @DeleteMapping("/{shortLink}")
    public DeleteLinkResponse delete(Principal principal, @PathVariable String shortLink) {
        return linkService.delete(principal.getName(), shortLink);
    }
}
