package org.example.pensionatbackend1.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
public class CustomerDto {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank @Email
    private String email;
}
