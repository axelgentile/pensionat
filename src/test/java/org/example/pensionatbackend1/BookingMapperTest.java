package org.example.pensionatbackend1;

import org.example.pensionatbackend1.Models.Booking;
import org.example.pensionatbackend1.Models.Customer;
import org.example.pensionatbackend1.Models.Room;
import org.example.pensionatbackend1.Models.modelenums.RoomType;
import org.example.pensionatbackend1.dto.BookingDto;
import org.example.pensionatbackend1.mapper.BookingMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingMapperTest {

    private final BookingMapper bookingMapper = new BookingMapper();

    @Test
    public void testToDto() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Erik");
        customer.setLastName("Erdhage");

        Room room = new Room();
        room.setId(2L);
        room.setRoomNumber(101);

        Booking booking = new Booking();
        booking.setId(3L);
        booking.setCustomer(customer);
        booking.setRoom(room);
        booking.setCheckInDate(LocalDate.of(2025, 5, 25));
        booking.setCheckOutDate(LocalDate.of(2025, 5, 28));

        BookingDto dto = bookingMapper.toDto(booking);

        assertEquals(3L, dto.getId());
        assertEquals(1L, dto.getCustomerId());
        assertEquals("Erik", dto.getCustomerFirstName());
        assertEquals("Erdhage", dto.getCustomerLastName());
        assertEquals(2L, dto.getRoomId());
        assertEquals(101, dto.getRoomNumber());
        assertEquals(LocalDate.of(2025, 5, 25), dto.getCheckInDate());
        assertEquals(LocalDate.of(2025, 5, 28), dto.getCheckOutDate());
    }
    @Test
    public void testToEntity() {
        BookingDto dto = new BookingDto();
        dto.setId(5L);
        dto.setCheckInDate(LocalDate.of(2025, 6, 10));
        dto.setCheckOutDate(LocalDate.of(2025, 6, 15));

        Customer customer = new Customer();
        customer.setId(10L);
        customer.setFirstName("Anna");
        customer.setLastName("Andersson");

        Room room = new Room();
        room.setId(20L);
        room.setRoomNumber(202);

        BookingMapper mapper = new BookingMapper();
        Booking booking = mapper.toEntity(dto, customer, room);

        assertEquals(5L, booking.getId());
        assertEquals(customer, booking.getCustomer());
        assertEquals(room, booking.getRoom());
        assertEquals(LocalDate.of(2025, 6, 10), booking.getCheckInDate());
        assertEquals(LocalDate.of(2025, 6, 15), booking.getCheckOutDate());
    }

}
