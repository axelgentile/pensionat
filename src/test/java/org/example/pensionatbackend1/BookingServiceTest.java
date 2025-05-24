package org.example.pensionatbackend1;

import org.example.pensionatbackend1.Models.Booking;
import org.example.pensionatbackend1.Models.Customer;
import org.example.pensionatbackend1.Models.Room;
import org.example.pensionatbackend1.Models.modelenums.RoomType;
import org.example.pensionatbackend1.dto.BookingDto;
import org.example.pensionatbackend1.mapper.BookingMapper;
import org.example.pensionatbackend1.repository.BookingRepository;
import org.example.pensionatbackend1.repository.CustomerRepository;
import org.example.pensionatbackend1.repository.RoomRepository;
import org.example.pensionatbackend1.service.BookingService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {


    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void getAllBookings_returnsBookingDtoList() {

        Booking booking = new Booking();
        BookingDto bookingDto = new BookingDto();

        when(bookingRepository.findAll()).thenReturn(List.of(booking));
        when(bookingMapper.toDto(booking)).thenReturn(bookingDto);

        List<BookingDto> result = bookingService.getAllBookings();

        assertEquals(1, result.size());
        verify(bookingRepository).findAll();
        verify(bookingMapper).toDto(booking);

    }

    @Test
    void createBooking_withValidInput_returnsBookingDto() {
        BookingDto dto = new BookingDto();
        dto.setRoomId(1L);
        dto.setCustomerId(1L);
        dto.setCheckInDate(LocalDate.now());
        dto.setCheckOutDate(LocalDate.now().plusDays(2));

        Room room = new Room();
        Customer customer = new Customer();
        Booking booking = new Booking();

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(bookingRepository.findOverlapping(anyLong(), any(), any())).thenReturn(List.of());
        when(bookingMapper.toEntity(dto, customer, room)).thenReturn(booking);
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingMapper.toDto(booking)).thenReturn(dto);

        BookingDto result = bookingService.createBooking(dto);

        assertNotNull(result);
        verify(bookingRepository).save(booking);
    }

    @Test
    void createBooking_withOverlappingBooking_throwsException() {
        BookingDto dto = new BookingDto();
        dto.setRoomId(1L);
        dto.setCustomerId(1L);
        dto.setCheckInDate(LocalDate.now());
        dto.setCheckOutDate(LocalDate.now().plusDays(2));

        Room room = new Room();
        Customer customer = new Customer();
        Booking existingBooking = new Booking();

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(bookingRepository.findOverlapping(anyLong(), any(), any())).thenReturn(List.of(existingBooking));

        assertThrows(IllegalArgumentException.class, () -> bookingService.createBooking(dto));
    }

    @Test
    void deleteBooking_withExistingId_callsRepositoryDelete() {
        Long id = 1L;
        when(bookingRepository.existsById(id)).thenReturn(true);

        bookingService.deleteBooking(id);

        verify(bookingRepository).deleteById(id);
    }

    @Test
    void deleteBooking_withNonExistingId_throwsException() {
        Long id = 99L;
        when(bookingRepository.existsById(id)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> bookingService.deleteBooking(id));
    }

    @Test
    void searchAvailableRooms_returnsAvailableRooms() {
        // Arrange
        Room room1 = new Room(1L, 101, RoomType.DOUBLE, 100.0, 1);
        Room room2 = new Room(2L, 102, RoomType.SINGLE, 80.0, 0);
        when(roomRepository.findAll()).thenReturn(List.of(room1, room2));
        when(bookingRepository.findOverlapping(anyLong(), any(), any())).thenReturn(List.of());

        List<Room> rooms = bookingService.searchAvailableRooms(LocalDate.now(), LocalDate.now().plusDays(3), 1);

        assertEquals(2, rooms.size());
    }

    @Test
    void getMaxCapacityForRoom_returnsCorrectCapacity() {
        Room singleRoom = new Room(1L, 101, RoomType.SINGLE, 80.0, 0);
        Room doubleRoomWithExtraBeds = new Room(2L, 102, RoomType.DOUBLE, 120.0, 2);

        int singleRoomCapacity = bookingService.getMaxCapacityForRoom(singleRoom);
        int doubleRoomCapacity = bookingService.getMaxCapacityForRoom(doubleRoomWithExtraBeds);

        assertEquals(1, singleRoomCapacity);
        assertEquals(4, doubleRoomCapacity);
    }
}




