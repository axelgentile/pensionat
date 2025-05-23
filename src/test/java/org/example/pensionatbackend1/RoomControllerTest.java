package org.example.pensionatbackend1;

import org.example.pensionatbackend1.Models.Room;
import org.example.pensionatbackend1.Models.modelenums.RoomType;
import org.example.pensionatbackend1.controller.RoomController;
import org.example.pensionatbackend1.dto.RoomDto;
import org.example.pensionatbackend1.service.RoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import jakarta.servlet.ServletException;


import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoomController.class)
public class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoomService roomService;

    @Test
    void GET_all_rooms_shouldReturnDtoList() throws Exception {
        // Arrange: service ger Room-entiteter
        Room r1 = new Room(1L, 101, RoomType.SINGLE, 895.0, 0);
        Room r2 = new Room(2L, 102, RoomType.DOUBLE, 1195.0, 2);
        when(roomService.getAllRooms()).thenReturn(List.of(r1, r2));

        // De DTO:er som controllern borde placera i modellen
        RoomDto d1 = new RoomDto(1L, 101, RoomType.SINGLE, 895.0, 0);
        RoomDto d2 = new RoomDto(2L, 102, RoomType.DOUBLE, 1195.0, 2);

        // Act & Assert
        mockMvc.perform(get("/rooms/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("rooms"))
                .andExpect(model().attributeExists("rooms"))
                .andExpect(model().attribute("rooms", List.of(d1, d2)));
    }


    @Test
    void GET_create_form_shouldReturnFormView() throws Exception {
        mockMvc.perform(get("/rooms/create")).andExpect(status().isOk()).andExpect(view().name("room-form")).andExpect(model().attributeExists("roomDto"));
    }

    @Test
    void POST_create_room_success_shouldRedirectWithFlash() throws Exception {
        // Arrange: låt service.createRoom inte kasta
        doNothing().when(roomService).createRoom(any(Room.class));

        // Act & Assert
        mockMvc.perform(post("/rooms/create").param("roomNumber", "103").param("roomType", "SINGLE").param("pricePerNight", "995.0").param("extraBeds", "1").contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/rooms/all")).andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    void POST_create_room_failure_shouldRedirectBackWithError() throws Exception {
        // Arrange: kasta IllegalArgumentException
        doThrow(new IllegalArgumentException("Testfel")).when(roomService).createRoom(any(Room.class));

        // Act & Assert
        mockMvc.perform(post("/rooms/create").param("roomNumber", "999").param("roomType", "DOUBLE").param("pricePerNight", "0.0").param("extraBeds", "10")  // ogiltigt, ger fel i service
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/rooms/create")).andExpect(flash().attribute("errormessage", "Testfel"));
    }

    @Test
    void POST_delete_room_shouldRedirectWithFlash() throws Exception {
        // Arrange: låt delete fungera utan fel
        doNothing().when(roomService).deleteRoomById(42L);

        // Act & Assert
        mockMvc.perform(post("/rooms/delete/42")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/rooms/all")).andExpect(flash().attributeExists("deleteMessage"));
    }

    @Test
    void POST_delete_room_missing_shouldThrowServletException() throws Exception {
        // Arrange: mocka så service kastar IllegalArgumentException
        doThrow(new IllegalArgumentException("Finns ej"))
                .when(roomService).deleteRoomById(99L);

        // Act & Assert: förvänta en ServletException
        ServletException ex = assertThrows(
                ServletException.class,
                () -> mockMvc.perform(post("/rooms/delete/99"))
        );

        // Kontrollera att det inbäddade undantaget är det vi kastade
        Throwable cause = ex.getCause();
        assertNotNull(cause);
        assertInstanceOf(IllegalArgumentException.class, cause);
        assertEquals("Finns ej", cause.getMessage());
    }
}