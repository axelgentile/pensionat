package org.example.pensionatbackend1.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
@Validated
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomSearchDto {
    @NotNull(message = "Check-in datum krävs")
    @FutureOrPresent(message = "Check-in måste vara idag eller senare")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate checkIn;

    @NotNull(message = "Check-out datum krävs")
    @Future(message = "Check-out måste vara efter check-in")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate checkOut;

    @Min(value = 1, message = "Minst en gäst krävs")
    @Max(value = 4, message = "Max 4 gäster")
    private int guests;
}
