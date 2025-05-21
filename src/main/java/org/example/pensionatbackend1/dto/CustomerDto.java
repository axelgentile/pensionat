package org.example.pensionatbackend1.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@AllArgsConstructor
@NoArgsConstructor
@Validated
@Data
public class CustomerDto {
    private Long id;
    @NotBlank(message = "Förnamn kan inte vara tomt")
    private String firstName;
    @NotBlank(message = "Efternamn kan inte vara tomt")
    private String lastName;
    @NotBlank(message = "Emailaddress kan inte vara tom") @Email(message = "Ogiltig emailaddress")
    private String email;
    @NotBlank(message = "Telefonnummer kan inte vara tomt")
    @Pattern(
            regexp = "^(\\+46|0)[\\s\\-]?[1-9][0-9]{1,2}[\\s\\-]?[0-9]{3,4}[\\s\\-]?[0-9]{3,4}$",
            message = "Ogiltigt telefonnummerformat")
    private String phoneNum;
}
