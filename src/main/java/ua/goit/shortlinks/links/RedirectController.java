package ua.goit.shortlinks.links;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/")
public class RedirectController {

    private final LinkService linkService;

    public RedirectController(LinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping("/{shortLink}")
    public ResponseEntity<Void> redirect(@PathVariable String shortLink) {
        System.out.println("shortLink = " + shortLink); // TO REMOVE LATER
        Link link = linkService.getByShortLink(shortLink);

        if (link == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (link.isDeleted()) {
            return new ResponseEntity<>(HttpStatus.GONE);
        } else if (link.getValidTo().isBefore(LocalDateTime.now())) {
            return new ResponseEntity<>(HttpStatus.GONE);
        } else {
            link.setCounter(link.getCounter() + 1);
            linkService.save(link);
            System.out.println("link.getOriginalLink() = " + link.getOriginalLink()); //TO REMOVE LATER
            return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                    .location(URI.create(link.getOriginalLink()))
                    .build();
        }
    }
}