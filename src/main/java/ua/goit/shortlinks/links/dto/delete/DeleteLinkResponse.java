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
        invalidLinkId,
        linkNotFound // Новая константа для обозначения того, что ссылка не найдена
    }

    public static DeleteLinkResponse success() {
        return builder().error(Error.ok).build();
    }

    public static DeleteLinkResponse failed(Error error) {
        return builder().error(error).build();
    }
}
