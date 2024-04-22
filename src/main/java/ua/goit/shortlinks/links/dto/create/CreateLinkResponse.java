package ua.goit.shortlinks.links.dto.create;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateLinkResponse  {
    private Error error;
    private String shortLink;
    private String originalLink;
    private long createdLinkResponse;

    public enum Error {
        ok,
        invalidOriginalLink,
        multipleLinksProvided,
        invalidLink,
        originalLinkAlreadyExists,

        invalidLinkFormat
    }

    public static CreateLinkResponse success(String shortLink, String originalLink) {
        return builder().error(Error.ok).shortLink(shortLink).originalLink(originalLink).build();
    }

    public static CreateLinkResponse failed(Error error) {
//        return builder().error(error).createdLinkId(-1L).build();
        return builder().error(error).createdLinkResponse(-1L).build();
    }
}
