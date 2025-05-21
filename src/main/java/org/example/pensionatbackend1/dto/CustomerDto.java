package org.example.pensionatbackend1.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
public class CustomerDto {
    @NotBlank(message = "Förnamn kan inte vara tomt")
    private String firstName;
    @NotBlank(message = "Efternamn kan inte vara tomt")
    private String lastName;
    @NotBlank(message = "Emailaddress kan inte vara tom") @Email(message = "Ogiltig emailaddress")
    private String email;
}
