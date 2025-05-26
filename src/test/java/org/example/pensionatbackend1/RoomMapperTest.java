package org.example.pensionatbackend1;

import org.example.pensionatbackend1.Models.Room;
import org.example.pensionatbackend1.Models.modelenums.RoomType;
import org.example.pensionatbackend1.dto.RoomDto;
import org.example.pensionatbackend1.mapper.RoomMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoomMapperTest {

    @Test
    void toDto_shouldMapAllFields() {
        // Arrange: en sample-Room
        Room room = new Room(
                17L,                 // id
                305,                 // roomNumber
                RoomType.DOUBLE,     // roomType
                1299.0,              // pricePerNight
                1                    // extraBeds
        );

        // Act
        RoomDto dto = RoomMapper.toDto(room);

        // Assert
        assertThat(dto.getId()).isEqualTo(17L);
        assertThat(dto.getRoomNumber()).isEqualTo(305);
        assertThat(dto.getRoomType()).isEqualTo(RoomType.DOUBLE);
        assertThat(dto.getPricePerNight()).isEqualTo(1299.0);
        assertThat(dto.getExtraBeds()).isEqualTo(1);
    }

    @Test
    void toEntity_shouldMapAllFields() {
        // Arrange: en sample-RoomDto
        RoomDto dto = new RoomDto(
                42L,               // id
                410,               // roomNumber
                RoomType.SINGLE,   // roomType
                899.0,             // pricePerNight
                0                  // extraBeds
        );

        // Act
        Room room = RoomMapper.toEntity(dto);

        // Assert
        assertThat(room.getId()).isEqualTo(42L);
        assertThat(room.getRoomNumber()).isEqualTo(410);
        assertThat(room.getRoomType()).isEqualTo(RoomType.SINGLE);
        assertThat(room.getPricePerNight()).isEqualTo(899.0);
        assertThat(room.getExtraBeds()).isEqualTo(0);
    }
}
