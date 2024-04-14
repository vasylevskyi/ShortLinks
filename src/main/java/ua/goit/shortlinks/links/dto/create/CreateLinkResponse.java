package ua.goit.shortlinks.links.dto.create;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateLinkResponse  {
    private Error error;

    private long createdLinkId;

    public enum Error {
        ok,
        invalidShortLink,
        invalidOriginalLink
    }//////////////ПРОВЕРИТЬ

    public static CreateLinkResponse success(long createdLinkId) {
        return builder().error(Error.ok).createdLinkId(createdLinkId).build();
    }

    public static CreateLinkResponse failed(Error error) {
        return builder().error(error).createdLinkId(-1L).build();
    }
}
