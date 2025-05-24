package org.example.pensionatbackend1;

import org.example.pensionatbackend1.Models.Room;
import org.example.pensionatbackend1.dto.RoomDto;
import org.example.pensionatbackend1.Models.modelenums.RoomType;
import org.example.pensionatbackend1.mapper.RoomMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoomMapperTest {

    @Test
    public void testToDto() {
        Room room = new Room();
        room.setId(1L);
        room.setRoomNumber(101);
        room.setRoomType(RoomType.DOUBLE);
        room.setPricePerNight(1200.0);
        room.setExtraBeds(1);

        RoomDto dto = RoomMapper.toDto(room);

        assertEquals(1L, dto.getId());
        assertEquals(101, dto.getRoomNumber());
        assertEquals(RoomType.DOUBLE, dto.getRoomType());
        assertEquals(1200.0, dto.getPricePerNight());
        assertEquals(1, dto.getExtraBeds());
    }

    @Test
    public void testToEntity() {
        RoomDto dto = new RoomDto();
        dto.setId(2L);
        dto.setRoomNumber(202);
        dto.setRoomType(RoomType.SINGLE);
        dto.setPricePerNight(950.0);
        dto.setExtraBeds(0);

        Room room = RoomMapper.toEntity(dto);

        assertEquals(2L, room.getId());
        assertEquals(202, room.getRoomNumber());
        assertEquals(RoomType.SINGLE, room.getRoomType());
        assertEquals(950.0, room.getPricePerNight());
        assertEquals(0, room.getExtraBeds());
    }
}
