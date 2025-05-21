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

    @NotBlank
    @Min(1)
    private Integer roomNumber;

    @NotBlank
    private RoomType roomType;

    @NotBlank
    @DecimalMin("0.0")
    private BigDecimal pricePerNight;

    @NotBlank
    @Min(0)
    @Max(2)
    private Integer extraBeds;
}
