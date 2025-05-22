package org.example.pensionatbackend1.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RoomSearchDto {

    @NotNull(message = "Antal gäster krävs")
    @Min(value = 1, message = "Minst 1 gäst krävs")
    private Integer guests;

    @NotNull(message = "Check-in datum krävs")
    @FutureOrPresent(message = "Check-in kan inte vara i det förflutna")
    private LocalDate checkInDate;

    @NotNull(message = "Check-out datum krävs")
    private LocalDate checkOutDate;
}