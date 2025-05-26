package org.example.pensionatbackend1;

import org.example.pensionatbackend1.Models.Room;
import org.example.pensionatbackend1.Models.modelenums.RoomType;
import org.example.pensionatbackend1.controller.RoomController;
import org.example.pensionatbackend1.service.RoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoomController.class)
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomService roomService;

    @Test
    void delete_success_shouldRedirectWithSuccessMessage() throws Exception {
        // Arrange: rummet finns och tas bort utan undantag
        when(roomService.getRoomById(42L)).thenReturn(new Room(42L, 101, RoomType.SINGLE, 800.0, 0));
        doNothing().when(roomService).deleteRoomById(42L);

        // Act & Assert
        mockMvc.perform(post("/rooms/delete/42"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rooms/all"))
                .andExpect(flash().attribute("successMessage", "Rummet har tagits bort."));
    }

    @Test
    void delete_notFound_shouldRedirectWithNotFoundError() throws Exception {
        // Arrange: getRoomById returnerar null
        when(roomService.getRoomById(99L)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/rooms/delete/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rooms/all"))
                .andExpect(flash().attribute("errorMessage", "Rummet finns inte."));

        // Verifiera att delete aldrig anropas
        verify(roomService, never()).deleteRoomById(anyLong());
    }

    @Test
    void delete_dataIntegrityViolation_shouldRedirectWithBookingError() throws Exception {
        // Arrange: rummet finns, men delete kastar DataIntegrityViolationException
        when(roomService.getRoomById(7L)).thenReturn(new Room(7L, 202, RoomType.DOUBLE, 1200.0, 1));
        doThrow(new DataIntegrityViolationException("FK constraint"))
                .when(roomService).deleteRoomById(7L);

        // Act & Assert
        mockMvc.perform(post("/rooms/delete/7"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rooms/all"))
                .andExpect(flash().attribute("errorMessage",
                        "Rummet kan inte tas bort eftersom det är bokat av en eller flera kunder."));
    }

    @Test
    void delete_otherException_shouldRedirectWithGenericError() throws Exception {
        // Arrange: rummet finns, delete kastar godtyckligt undantag
        when(roomService.getRoomById(8L)).thenReturn(new Room(8L, 303, RoomType.SINGLE, 900.0, 0));
        doThrow(new RuntimeException("något gick fel"))
                .when(roomService).deleteRoomById(8L);

        // Act & Assert
        mockMvc.perform(post("/rooms/delete/8"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rooms/all"))
                .andExpect(flash().attribute("errorMessage",
                        "Ett fel uppstod vid borttagning av rummet."));
    }
}
