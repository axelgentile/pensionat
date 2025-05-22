package org.example.pensionatbackend1.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Validated
@Data
public class BookingDto {
    private Long id;

    @NotNull(message = "Kund-ID krävs")
    private Long customerId;

    @NotNull(message = "Rums-ID krävs")
    private Long roomId;

    private String customerFirstName;
    private String customerLastName;
    private Integer roomNumber;

    @NotNull(message = "Startdatum krävs")
    @FutureOrPresent(message = "Startdatum kan inte vara i det förflutna")
    private LocalDate checkInDate;

    @NotNull(message = "Slutdatum krävs")
    private LocalDate checkOutDate;

}
