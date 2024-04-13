package ua.goit.shortlinks.auth.dto.registration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.Data;

@Data
public class RegistrationRequest {

     @NotBlank(message = "Email cannot be empty")
     @Email(message = "Email is not valid")
     private String email;

     @NotBlank(message = "Password cannot be empty")
     private String password;

}
