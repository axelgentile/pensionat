package org.example.pensionatbackend1.mapper;


import org.example.pensionatbackend1.Models.Booking;
import org.example.pensionatbackend1.Models.Room;
import org.example.pensionatbackend1.Models.Customer;
import org.example.pensionatbackend1.dto.BookingDto;
import org.springframework.stereotype.Component;


@Component
public class BookingMapper {

    public BookingDto toDto(Booking booking) {
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setCustomerId(booking.getCustomer().getId());
        dto.setCustomerFirstName(booking.getCustomer().getFirstName());
        dto.setCustomerLastName(booking.getCustomer().getLastName());
        dto.setRoomId(booking.getRoom().getId());
        dto.setRoomNumber(booking.getRoom().getRoomNumber());
        dto.setCheckInDate(booking.getCheckInDate());
        dto.setCheckOutDate(booking.getCheckOutDate());
        return dto;
    }

    public Booking toEntity(BookingDto dto, Customer customer, Room room) {
        Booking booking = new Booking();
        booking.setId(dto.getId());
        booking.setCustomer(customer);
        booking.setRoom(room);
        booking.setCheckInDate(dto.getCheckInDate());
        booking.setCheckOutDate(dto.getCheckOutDate());
        return booking;
    }
}

