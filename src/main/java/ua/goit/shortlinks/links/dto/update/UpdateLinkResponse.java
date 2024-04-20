package ua.goit.shortlinks.links.dto.update;

import ua.goit.shortlinks.links.Link;
import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class UpdateLinkResponse {
    private Error error;

    private Link updatedLink;

    public enum Error {
        ok,
        linkAlreadyExist,
        linkNotFound,
        NoLinkAvailable,
        invalidNewLink,
        invalidShortLinkLength,
        invalidOriginalLinkLength,
        linkNotExist
    }

    public static UpdateLinkResponse success(Link updatedLink) {
        return UpdateLinkResponse.builder().error(Error.ok).updatedLink(updatedLink).build();
    }

    public static UpdateLinkResponse failed(Error error) {
        return UpdateLinkResponse.builder().error(error).updatedLink(null).build();
    }
}
