package ua.goit.shortlinks.links.dto.create;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateLinkResponse  {
    private Error error;
    private String shortLink; //Добавлено 18.04
    private long createdLinkId;

    public enum Error {
        ok,
//        invalidShortLink, //18 04 причина, не используеться
        invalidOriginalLink
    }

//    public static CreateLinkResponse success(long createdLinkId) {
//        return builder().error(Error.ok).createdLinkId(createdLinkId).build();
//    } comented 18 04
public static CreateLinkResponse success(String shortLink) {
    return builder().error(Error.ok).shortLink(shortLink).build();
}

    public static CreateLinkResponse failed(Error error) {
        return builder().error(error).createdLinkId(-1L).build();
    }
}
