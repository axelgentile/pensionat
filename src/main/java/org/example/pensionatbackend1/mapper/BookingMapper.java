package org.example.pensionatbackend1.mapper;


import org.example.pensionatbackend1.Models.Booking;
import org.example.pensionatbackend1.Models.Customer;
import org.example.pensionatbackend1.dto.BookingDto;
import org.example.pensionatbackend1.dto.CustomerDto;
import org.springframework.stereotype.Component;


@Component
public class BookingMapper {
    public static BookingDto toDto(Booking booking) {
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setCustomerId(booking.getCustomer().getId());
        dto.setRoomId(booking.getRoom().getId());
        dto.setCheckInDate(booking.getCheckInDate());
        dto.setCheckOutDate(booking.getCheckOutDate());
        return dto;
    }
    public Booking toEntity(BookingDto dto) {
        Booking booking = new Booking();
        booking.setId(dto.getId());
        booking.setCheckInDate(dto.getCheckInDate());
        booking.setCheckOutDate(dto.getCheckOutDate());
        return booking;
    }
}
