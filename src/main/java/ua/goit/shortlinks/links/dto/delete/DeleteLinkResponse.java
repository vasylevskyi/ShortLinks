package ua.goit.shortlinks.links.dto.delete;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DeleteLinkResponse {
    private Error error;

    public enum Error {
        ok,
        insufficientPrivileges,
        invalidLinkId
    }//////////////ПРОВЕРИТЬ

    public static DeleteLinkResponse success() {
        return builder().error(Error.ok).build();
    }

    public static DeleteLinkResponse failed(Error error) {
        return builder().error(error).build();
    }
}
