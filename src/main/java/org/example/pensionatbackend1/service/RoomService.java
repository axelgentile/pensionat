package org.example.pensionatbackend1.service;


import org.example.pensionatbackend1.Models.Room;
import org.example.pensionatbackend1.Models.modelenums.RoomType;
import org.example.pensionatbackend1.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
public class RoomService{
    @Autowired
    private RoomRepository roomRepository;

    public List<Room> getAllRooms(){
        return roomRepository.findAll();
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

    public Room createRoom(Room room){
        validateBeds(room);
        return roomRepository.save(room);
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

}
