package ua.goit.shortlinks.notes.dto.get;

import ua.goit.shortlinks.notes.Link;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class GetUserLinksResponse {
    private Error error;

    private List<Link> userLinks;

    public enum Error {
        ok
    }

    public static GetUserLinksResponse success(List<Link> userLinks) {
        return builder().error(Error.ok).userLinks(userLinks).build();
    }

    public static GetUserLinksResponse failed(Error error) {
        return builder().error(error).userLinks(null).build();
    }
}
