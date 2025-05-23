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

}
