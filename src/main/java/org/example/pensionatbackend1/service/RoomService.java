package org.example.pensionatbackend1.service;


import org.example.pensionatbackend1.Models.Room;
import org.example.pensionatbackend1.Models.modelenums.RoomType;
import org.example.pensionatbackend1.dto.RoomDto;
import org.example.pensionatbackend1.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService{
    @Autowired
    private RoomRepository roomRepository;

    public List<Room> getAllRooms(){
        return roomRepository.findAll();
    }

    public void validateRoomNumber(Room room){
        Room existingRoom = roomRepository.findByRoomNumber(room.getRoomNumber());
        if (existingRoom != null){
            throw new IllegalArgumentException("Rummet finns redan.");
        }
    }

    public void validateBeds(Room room){
        if (room.getRoomType() == RoomType.SINGLE && room.getExtraBeds() > 0){
            throw new IllegalArgumentException("Enkelrum kan inte ha extra sängar");
        }
        if (room.getRoomType() == RoomType.DOUBLE && room.getExtraBeds() > 2){
            throw new IllegalArgumentException("Dubbelrum kan max ha 2 sängar");
        }
        if (room.getExtraBeds() < 0){
            throw new IllegalArgumentException("Det kan inte vara negativa sängar.");
        }
    }

    public void createRoom(Room room){
        validateBeds(room);
        validateRoomNumber(room);
        roomRepository.save(room);
    }

    public Room updateRoom(Long id, Room room){
        Room existingRoom = roomRepository.findById(id).orElseThrow();
        existingRoom.setRoomNumber(room.getRoomNumber());
        existingRoom.setRoomType(room.getRoomType());
        existingRoom.setPricePerNight(room.getPricePerNight());
        existingRoom.setExtraBeds(room.getExtraBeds());
        validateBeds(existingRoom);
        return roomRepository.save(existingRoom);
    }

    public List<RoomDto> getAllRoomDtos() {
        return getAllRooms().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    private RoomDto convertToDto(Room room) {
        return new RoomDto(
                room.getId(),
                room.getRoomNumber(),
                room.getRoomType(),
                room.getPricePerNight(),
                room.getExtraBeds()
        );
    }

}
