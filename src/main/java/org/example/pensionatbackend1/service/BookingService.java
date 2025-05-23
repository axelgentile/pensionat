package org.example.pensionatbackend1.service;

import org.example.pensionatbackend1.Models.Booking;
import org.example.pensionatbackend1.Models.Customer;
import org.example.pensionatbackend1.Models.Room;
import org.example.pensionatbackend1.Models.modelenums.RoomType;
import org.example.pensionatbackend1.dto.BookingDto;
import org.example.pensionatbackend1.mapper.BookingMapper;
import org.example.pensionatbackend1.repository.BookingRepository;
import org.example.pensionatbackend1.repository.CustomerRepository;
import org.example.pensionatbackend1.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private BookingMapper bookingMapper;
    @Autowired
    private RoomRepository roomRepository;

    //Hämta alla bokningar som DTO-lista
    public List<BookingDto> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(bookingMapper::toDto).toList();
    }

    public BookingDto createBooking(BookingDto dto) {
        //Hämta och kontrollera rum samt kund
        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Rum ej hittat: " + dto.getRoomId()));

        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Kund ej hittad: " + dto.getCustomerId()));

        // Överlappningskontroll
        List<Booking> overlaps = bookingRepository.findOverlapping(
                room.getId(), dto.getCheckInDate(), dto.getCheckOutDate());
        if (!overlaps.isEmpty()) {
            throw new IllegalArgumentException("Överlappande bokning! Rummet är redan bokat.");
        }

        //Mappa - sätt relationer & spara bokning
        Booking booking = bookingMapper.toEntity(dto, customer, room);
        Booking saved = bookingRepository.save(booking);
        return bookingMapper.toDto(saved);
    }

    public BookingDto updateBooking(BookingDto dto) {
        Booking existingBooking = bookingRepository.findById(dto.getId())
                .orElseThrow(() -> new NoSuchElementException("Inget bokat rum med ID " + dto.getId()));

        if (!dto.getCheckOutDate().isAfter(existingBooking.getCheckOutDate())) {
            throw new NoSuchElementException("Slutdatum måste vara efter startdatum");
        }

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Rum ej hittat: " + dto.getRoomId()));

        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Kund ej hittad: " + dto.getCustomerId()));


        List<Booking> overlaps = bookingRepository.findOverlapping(
                        room.getId(), dto.getCheckInDate(), dto.getCheckOutDate()).stream()
                .filter(b -> !b.getId().equals(dto.getId()))
                .toList();
        if (!overlaps.isEmpty()) {
            throw new IllegalArgumentException("Dubbelbokning vid uppdatering! Rummet är redan bokat.");
        }

        existingBooking.setRoom(room);
        existingBooking.setCustomer(customer);
        existingBooking.setCheckInDate(dto.getCheckInDate());
        existingBooking.setCheckOutDate(dto.getCheckOutDate());

        Booking saved = bookingRepository.save(existingBooking);

        return bookingMapper.toDto(saved);
    }

    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new IllegalArgumentException("Bokningen existeras inte");
        }
        bookingRepository.deleteById(id);
    }


    public List<Room> searchAvailableRooms(LocalDate checkIn, LocalDate checkOut, int guests) {
        List<Room> suitableRooms = roomRepository.findAll().stream()
                .filter(room -> getMaxCapacityForRoom(room) >= guests)
                .toList();

        return suitableRooms.stream().filter(room -> bookingRepository.findOverlapping(room.getId(), checkIn, checkOut).isEmpty()).toList();
    }

    public int getMaxCapacityForRoom(Room room) {
        //Returnera max antal personer ett rum klarar baserat på rumstyp och extrabäddar
        int base = switch (room.getRoomType()) {
            case SINGLE -> 1;
            case DOUBLE -> 2;
        };
        int extraBeds = room.getRoomType() == RoomType.DOUBLE ? room.getExtraBeds() : 0;

        return base + extraBeds;
    }
}
