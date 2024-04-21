package ua.goit.shortlinks.auth.dto.registration;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RegistrationResponse {
    private Error error;
    private String message;

    public enum Error {
        ok,
        userAlreadyExists,
        invalidEmail,
        invalidPassword,
    }

    public static RegistrationResponse success() {
        return builder().error(Error.ok).build();
    }

    public static RegistrationResponse failedAndMessage(Error error, String message) {
        return builder().error(error).message(message).build();
    }
}