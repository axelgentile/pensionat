package org.example.pensionatbackend1.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import org.example.pensionatbackend1.Models.modelenums.RoomType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
    private Long id;

    @NotBlank(message = "Måste fyllas i")
    @Min(1)
    private Integer roomNumber;

    @NotBlank(message = "Måste fyllas i")
    private RoomType roomType;

    @NotBlank
    @DecimalMin("0.0")
    private double pricePerNight;

    @NotBlank
    @Min(0)
    @Max(2)
    private Integer extraBeds;
}
