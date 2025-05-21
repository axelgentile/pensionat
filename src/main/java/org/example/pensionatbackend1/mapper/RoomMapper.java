package org.example.pensionatbackend1.mapper;


import org.example.pensionatbackend1.Models.Room;
import org.example.pensionatbackend1.dto.RoomDto;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {
    public static RoomDto toDto(Room room) {
        RoomDto dto = new RoomDto();
        dto.setId(room.getId());
        dto.setExtraBeds(room.getExtraBeds());
        dto.setRoomType(room.getRoomType());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setPricePerNight(room.getPricePerNight());
        return dto;
    }
    public static Room toEntity(RoomDto dto) {
        Room room = new Room();
        room.setId(dto.getId());
        room.setExtraBeds(dto.getExtraBeds());
        room.setRoomType(dto.getRoomType());
        room.setRoomNumber(dto.getRoomNumber());
        room.setPricePerNight(dto.getPricePerNight());
        return room;
    }

}
