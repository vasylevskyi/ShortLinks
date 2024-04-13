package ua.goit.shortlinks.notes.dto.update;

import ua.goit.shortlinks.notes.Link;
import lombok.Builder;
import lombok.Data;
///????????????????????????????????????????????????????????????
@Builder
@Data
public class UpdateLinkResponse {
    private Error error;

    private Link updatedLink;

    public enum Error {
        ok,
        insufficientPrivileges,
        invalidLinkId,
        invalidShortLinkLength,
        invalidOriginalLinkLength
    }//////////////ПРОВЕРИТЬ

    public static UpdateLinkResponse success(Link updatedLink) {
        return UpdateLinkResponse.builder().error(Error.ok).updatedLink(updatedLink).build();
    }

    public static UpdateLinkResponse failed(Error error) {
        return UpdateLinkResponse.builder().error(error).updatedLink(null).build();
    }
}
