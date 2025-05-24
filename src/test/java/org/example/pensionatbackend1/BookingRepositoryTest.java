package org.example.pensionatbackend1;

import org.example.pensionatbackend1.Models.Booking;
import org.example.pensionatbackend1.Models.Customer;
import org.example.pensionatbackend1.Models.Room;
import org.example.pensionatbackend1.Models.modelenums.RoomType;
import org.example.pensionatbackend1.repository.BookingRepository;
import org.example.pensionatbackend1.repository.CustomerRepository;
import org.example.pensionatbackend1.repository.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @DisplayName("findOverlapping ska hitta överlappande bokningar")
    void findOverlapping_findsOverlappingBookings() {
        Room room = new Room(null, 101, RoomType.DOUBLE, 100, 1);
        room = roomRepository.save(room);

        Customer customer = new Customer();
        customer.setFirstName("Test");
        customer.setLastName("User");
        customer.setEmail("test@test.com");
        customer.setPhoneNum("0701234567");
        customer = customerRepository.save(customer);

        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setCustomer(customer);
        booking.setCheckInDate(LocalDate.of(2025, 6, 1));
        booking.setCheckOutDate(LocalDate.of(2025, 6, 5));
        booking = bookingRepository.save(booking);

        List<Booking> overlaps = bookingRepository.findOverlapping(
                room.getId(),
                LocalDate.of(2025, 6, 3),
                LocalDate.of(2025, 6, 7)
        );

        assertEquals(1, overlaps.size());
        assertEquals(booking.getId(), overlaps.get(0).getId());
    }

    @Test
    @DisplayName("findOverlapping returnerar tom lista om ingen överlappning finns")
    void findOverlapping_noOverlap_returnsEmptyList() {
        Room room = new Room(null, 102, RoomType.DOUBLE, 100, 1);
        room = roomRepository.save(room);

        Customer customer = new Customer();
        customer.setFirstName("Test2");
        customer.setLastName("User2");
        customer.setEmail("test2@test.com");
        customer.setPhoneNum("0701234567");
        customer = customerRepository.save(customer);

        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setCustomer(customer);
        booking.setCheckInDate(LocalDate.of(2025, 6, 1));
        booking.setCheckOutDate(LocalDate.of(2025, 6, 5));
        booking = bookingRepository.save(booking);

        List<Booking> overlaps = bookingRepository.findOverlapping(
                room.getId(),
                LocalDate.of(2025, 6, 5),
                LocalDate.of(2025, 6, 10)
        );

        assertTrue(overlaps.isEmpty());
    }

    @Test
    @DisplayName("existsByCustomerId returnerar true om kund har bokning")
    void existsByCustomerId_withBooking_returnsTrue() {
        Room room = new Room(null, 103, RoomType.SINGLE, 80, 0);
        room = roomRepository.save(room);

        Customer customer = new Customer();
        customer.setFirstName("Has");
        customer.setLastName("Booking");
        customer.setEmail("has@booking.com");
        customer.setPhoneNum("0701234567");
        customer = customerRepository.save(customer);

        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setCustomer(customer);
        booking.setCheckInDate(LocalDate.of(2025, 6, 10));
        booking.setCheckOutDate(LocalDate.of(2025, 6, 12));
        booking = bookingRepository.save(booking);

        assertTrue(bookingRepository.existsByCustomerId(customer.getId()));
    }

    @Test
    @DisplayName("existsByCustomerId returnerar false om kund saknar bokning")
    void existsByCustomerId_withoutBooking_returnsFalse() {
        Customer customer = new Customer();
        customer.setFirstName("No");
        customer.setLastName("Booking");
        customer.setEmail("no@booking.com");
        customer.setPhoneNum("0701234567");
        customer = customerRepository.save(customer);

        assertFalse(bookingRepository.existsByCustomerId(customer.getId()));
    }
}