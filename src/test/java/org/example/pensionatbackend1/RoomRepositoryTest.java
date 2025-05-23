package org.example.pensionatbackend1;

import org.example.pensionatbackend1.Models.Room;
import org.example.pensionatbackend1.Models.modelenums.RoomType;
import org.example.pensionatbackend1.repository.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RoomRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    @Test
    @DisplayName("Save a Room and find it by ID")
    void saveAndFindById() {
        // Arrange
        Room room = new Room(null, 150, RoomType.SINGLE, 750.0, 0);
        // Act
        Room saved    = roomRepository.save(room);
        Optional<Room> fetched = roomRepository.findById(saved.getId());
        // Assert
        assertThat(fetched).isPresent();
        assertThat(fetched.get().getRoomNumber()).isEqualTo(150);
        assertThat(fetched.get().getRoomType()).isEqualTo(RoomType.SINGLE);
    }

    @Test
    @DisplayName("findByRoomNumber returns the correct Room")
    void findByRoomNumber() {
        // Arrange
        Room r1 = new Room(null, 201, RoomType.DOUBLE, 1200.0, 1);
        roomRepository.save(r1);
        // Act
        Room fetched = roomRepository.findByRoomNumber(201);
        // Assert
        assertThat(fetched).isNotNull();
        assertThat(fetched.getRoomNumber()).isEqualTo(201);
        assertThat(fetched.getPricePerNight()).isEqualTo(1200.0);
    }

    @Test
    @DisplayName("findAll returns all saved Rooms")
    void findAllReturnsAll() {
        // Arrange
        Room r1 = new Room(null, 301, RoomType.SINGLE, 900.0, 0);
        Room r2 = new Room(null, 302, RoomType.DOUBLE, 1100.0, 2);
        roomRepository.saveAll(List.of(r1, r2));
        // Act
        List<Room> rooms = roomRepository.findAll();
        // Assert
        assertThat(rooms).hasSize(2)
                .extracting(Room::getRoomNumber)
                .containsExactlyInAnyOrder(301, 302);
    }

    @Test
    @DisplayName("deleteById removes the Room")
    void deleteByIdRemoves() {
        // Arrange
        Room room = roomRepository.save(new Room(null, 401, RoomType.SINGLE, 850.0, 0));
        Long id = room.getId();
        // Act
        roomRepository.deleteById(id);
        Optional<Room> afterDelete = roomRepository.findById(id);
        // Assert
        assertThat(afterDelete).isEmpty();
    }
}
