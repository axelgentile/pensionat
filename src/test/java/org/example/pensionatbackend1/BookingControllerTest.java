package org.example.pensionatbackend1;

import org.example.pensionatbackend1.Models.Room;
import org.example.pensionatbackend1.Models.modelenums.RoomType;
import org.example.pensionatbackend1.controller.BookingController;
import org.example.pensionatbackend1.dto.BookingDto;
import org.example.pensionatbackend1.service.BookingService;
import org.example.pensionatbackend1.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private CustomerService customerService;

    @Test
    void showAllBookings_returnsViewWithBookings() throws Exception {
        List<BookingDto> bookings = List.of(new BookingDto(), new BookingDto());
        when(bookingService.getAllBookings()).thenReturn(bookings);

        mockMvc.perform(get("/bookings/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("all-bookings"))
                .andExpect(model().attribute("bookings", bookings));
    }

    @Test
    void showSearchForm_returnsViewWithRoomSearchDto() throws Exception {
        mockMvc.perform(get("/bookings/search"))
                .andExpect(status().isOk())
                .andExpect(view().name("search-rooms"))
                .andExpect(model().attributeExists("roomSearchDto"));
    }

    @Test
    void searchAvailableRooms_validationErrors_returnsFormWithErrors() throws Exception {
        mockMvc.perform(post("/bookings/searchAvailableRooms")
                        .param("checkIn", "")
                        .param("checkOut", "")
                        .param("guests", "0"))
                .andExpect(status().isOk())
                .andExpect(view().name("search-rooms"))
                .andExpect(model().attributeHasFieldErrors("roomSearchDto", "checkIn", "checkOut", "guests"));
    }

    @Test
    void searchAvailableRooms_validInput_returnsAvailableRoomsView() throws Exception {
        List<Room> rooms = List.of(
                new Room(1L, 101, RoomType.DOUBLE, 100, 1),
                new Room(2L, 102, RoomType.SINGLE, 80, 0)
        );

        when(bookingService.searchAvailableRooms(any(), any(), anyInt())).thenReturn(rooms);
        when(customerService.getAllCustomers()).thenReturn(List.of());

        mockMvc.perform(post("/bookings/searchAvailableRooms")
                        .param("checkIn", "2025-06-01")
                        .param("checkOut", "2025-06-05")
                        .param("guests", "2"))
                .andExpect(status().isOk())
                .andExpect(view().name("available-rooms"))
                .andExpect(model().attribute("availableRooms", hasSize(2)))
                .andExpect(model().attribute("checkIn", LocalDate.of(2025, 6, 1)))
                .andExpect(model().attribute("checkOut", LocalDate.of(2025, 6, 5)))
                .andExpect(model().attribute("guests", 2));
    }

    @Test
    void searchAvailableRooms_noRoomsAvailable_returnsEmptyList() throws Exception {
        when(bookingService.searchAvailableRooms(any(), any(), anyInt())).thenReturn(List.of());

        mockMvc.perform(post("/bookings/searchAvailableRooms")
                        .param("checkIn", "2025-06-01")
                        .param("checkOut", "2025-06-05")
                        .param("guests", "2"))
                .andExpect(status().isOk())
                .andExpect(view().name("available-rooms"))
                .andExpect(model().attribute("availableRooms", hasSize(0)));
    }

    @Test
    void cancelBooking_withValidId_redirectsWithSuccessMessage() throws Exception {
        mockMvc.perform(post("/bookings/delete/{id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bookings/all"))
                .andExpect(flash().attributeExists("bookingmessage"));

        verify(bookingService).deleteBooking(1L);
    }

    @Test
    void cancelBooking_withInvalidId_redirectsWithErrorMessage() throws Exception {
        doThrow(new IllegalArgumentException("Bokning ej hittad")).when(bookingService).deleteBooking(99L);

        mockMvc.perform(post("/bookings/delete/{id}", 99L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bookings/all"))
                .andExpect(flash().attributeExists("errorMessage"));

        verify(bookingService).deleteBooking(99L);
    }

    @Test
    void createBooking_validInput_redirectsWithSuccessMessage() throws Exception {
        mockMvc.perform(post("/bookings/book")
                        .param("roomId", "1")
                        .param("customerId", "2")
                        .param("checkInDate", "2025-06-01")
                        .param("checkOutDate", "2025-06-05"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bookings/all"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(bookingService).createBooking(any());
    }

    @Test
    void createBooking_serviceThrowsException_returnsViewWithErrorMessage() throws Exception {
        doThrow(new IllegalArgumentException("Överlappande bokning"))
                .when(bookingService).createBooking(any());

        mockMvc.perform(post("/bookings/book")
                        .param("roomId", "1")
                        .param("customerId", "2")
                        .param("checkInDate", "2025-06-01")
                        .param("checkOutDate", "2025-06-05"))
                .andExpect(status().isOk())
                .andExpect(view().name("available-rooms"))
                .andExpect(model().attributeExists("errorMessage"));

        verify(bookingService).createBooking(any());
    }
}