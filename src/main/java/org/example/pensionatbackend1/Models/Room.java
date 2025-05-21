package org.example.pensionatbackend1.Models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.pensionatbackend1.Models.modelenums.RoomType;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue
    private Long id;
    @NotNull(message = "Rumsnummer får inte vara tomt.")
    private Integer roomNumber;

    @NotNull(message = "Rumstyp får inte vara tomt")
    private RoomType roomType;
    @NotNull
    private BigDecimal pricePerNight;

    @Min(value = 0,message = "Extra sängar får inte vara mindre än 0.")
    @Max(value = 2,message = "Extra sängar får max vara 2")
    private int extraBeds;
}
