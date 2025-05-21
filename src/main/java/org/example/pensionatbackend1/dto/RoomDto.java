package org.example.pensionatbackend1.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import org.example.pensionatbackend1.Models.modelenums.RoomType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
    private Long id;

    @NotNull
    @Min(1)
    private Integer roomNumber;

    @NotNull
    private RoomType roomType;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal pricePerNight;

    @NotNull
    @Min(0)
    @Max(2)
    private Integer extraBeds;
}
