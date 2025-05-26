package org.example.pensionatbackend1;

import org.example.pensionatbackend1.Models.Room;
import org.example.pensionatbackend1.Models.modelenums.RoomType;
import org.example.pensionatbackend1.repository.BookingRepository;
import org.example.pensionatbackend1.repository.RoomRepository;
import org.example.pensionatbackend1.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private RoomService roomService;

    private Room singleRoom;
    private Room doubleRoom;

    @BeforeEach
    void setUp() {
        singleRoom = new Room(null, 101, RoomType.SINGLE, 895.0, 0);
        doubleRoom = new Room(null, 102, RoomType.DOUBLE, 1195.0, 2);
    }

    @Test
    void getAllRooms_returnsAllFromRepository() {
        when(roomRepository.findAll()).thenReturn(List.of(singleRoom, doubleRoom));

        List<Room> result = roomService.getAllRooms();

        assertThat(result).containsExactly(singleRoom, doubleRoom);
        verify(roomRepository).findAll();
    }

    @Test
    void validateRoomNumber_noExistingRoom_doesNotThrow() {
        when(roomRepository.findByRoomNumber(101)).thenReturn(null);

        assertThatCode(() -> roomService.validateRoomNumber(singleRoom))
                .doesNotThrowAnyException();
    }

    @Test
    void validateRoomNumber_existingRoom_throwsException() {
        when(roomRepository.findByRoomNumber(101)).thenReturn(singleRoom);

        assertThatThrownBy(() -> roomService.validateRoomNumber(singleRoom))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Rummet finns redan.");
    }

    @Test
    void validateBeds_singleRoomWithNoExtraBeds_doesNotThrow() {
        assertThatCode(() -> roomService.validateBeds(singleRoom))
                .doesNotThrowAnyException();
    }

    @Test
    void validateBeds_singleRoomWithExtraBeds_throwsException() {
        Room invalid = new Room(null, 103, RoomType.SINGLE, 895.0, 1);

        assertThatThrownBy(() -> roomService.validateBeds(invalid))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Enkelrum kan inte ha extra sängar");
    }

    @Test
    void validateBeds_doubleRoomWithTooManyExtraBeds_throwsException() {
        Room invalid = new Room(null, 104, RoomType.DOUBLE, 1195.0, 3);

        assertThatThrownBy(() -> roomService.validateBeds(invalid))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Dubbelrum kan max ha 2 sängar");
    }

    @Test
    void validateBeds_negativeExtraBeds_throwsException() {
        Room invalid = new Room(null, 105, RoomType.DOUBLE, 1195.0, -1);

        assertThatThrownBy(() -> roomService.validateBeds(invalid))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Det kan inte vara negativa sängar.");
    }

    @Test
    void createRoom_validRoom_callsSave() {
        when(roomRepository.findByRoomNumber(101)).thenReturn(null);
        // validateBeds(singleRoom) gör ingen exception
        roomService.createRoom(singleRoom);

        verify(roomRepository).save(singleRoom);
    }

    @Test
    void createRoom_duplicateRoomNumber_throwsException() {
        when(roomRepository.findByRoomNumber(101)).thenReturn(singleRoom);

        assertThatThrownBy(() -> roomService.createRoom(singleRoom))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Rummet finns redan.");

        verify(roomRepository, never()).save(any());
    }

    @Test
    void createRoom_invalidBeds_throwsException() {
        Room invalid = new Room(null, 106, RoomType.SINGLE, 895.0, 1);

        assertThatThrownBy(() -> roomService.createRoom(invalid))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Enkelrum kan inte ha extra sängar");

        verify(roomRepository, never()).save(any());
    }

    @Test
    void deleteRoomById_callsRepositoryDelete() {
        roomService.deleteRoomById(42L);

        verify(roomRepository).deleteById(42L);
    }
}
