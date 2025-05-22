package org.example.pensionatbackend1.service;

import org.example.pensionatbackend1.Models.Booking;
import org.example.pensionatbackend1.Models.Room;
import org.example.pensionatbackend1.dto.BookingDto;
import org.example.pensionatbackend1.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    public List<Booking> getAllBookings(){
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        // TODO: Logik för att hämta bokning med ID

        return null;
    }

    public Booking createBooking(BookingDto dto) {
        // TODO: Logik för att skapa bokning

        return null;
    }

    public Booking updateBooking(BookingDto dto) {
        // TODO: Logik för att uppdatera bokning

        return null;
    }

    public void deleteBooking(Long id) {
        // TODO: Logik för att radera bokning
    }

    public boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        // TODO: Logik för att kolla om ett rum finns ledigt

        return true;
    }

    public List<Room> searchAvailableRooms(LocalDate checkIn, LocalDate checkOut, int guests) {
        // TODO: Logik för att hitta lediga rum

        return null;
    }

    public int getMaxCapacityForRoom(Room room) {
        // TODO: Returnera max antal personer ett rum klarar baserat på rumstyp och extrabäddar

        return 0;
    }

}
