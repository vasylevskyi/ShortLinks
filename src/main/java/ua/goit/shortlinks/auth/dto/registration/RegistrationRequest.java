package ua.goit.shortlinks.auth.dto.registration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationRequest {

     @NotBlank(message = "Email cannot be empty")
     @Email(message = "Email is not valid")
     private String email;

     @NotBlank(message = "Password cannot be empty")
     @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$", message = "The password must contain at least one number, one small and one large letter, and at least 8 characters")
     private String password;

}
