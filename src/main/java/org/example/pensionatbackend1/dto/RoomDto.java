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

    @NotNull(message = "Rumsnummer måste fyllas i")
    @Min(value = 1, message = "Rumsnummer måste vara minst 1")
    private Integer roomNumber;

    @NotNull(message = "Rumstyp måste väljas")
    private RoomType roomType;

    @NotNull(message = "Pris per natt måste fyllas i")
    @DecimalMin(value = "0.0", message = "Priset måste vara minst 0")
    private Double pricePerNight;

    @NotNull(message = "Antal extra sängar måste anges")
    @Min(value = 0, message = "Antal extra sängar kan inte vara negativt")
    @Max(value = 2, message = "Max 2 extra sängar tillåtna")
    private Integer extraBeds;
}
