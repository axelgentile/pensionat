package org.example.pensionatbackend1.service;


import org.example.pensionatbackend1.Models.Room;
import org.example.pensionatbackend1.Models.modelenums.RoomType;
import org.example.pensionatbackend1.repository.BookingRepository;
import org.example.pensionatbackend1.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RoomService{
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BookingRepository bookingRepository;

    public List<Room> getAllRooms(){
        return roomRepository.findAll();
    }
    public Room getRoomById(Long id){
        return roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rummet finns inte."));
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
        // Sätt extraBeds enligt rumstyp
        if (room.getRoomType() == RoomType.SINGLE) {
            room.setExtraBeds(0);
        } else if (room.getRoomType() == RoomType.DOUBLE) {
            room.setExtraBeds(2);
        }

        validateBeds(room);
        validateRoomNumber(room);
        roomRepository.save(room);
    }
    public void deleteRoomById(Long id){
        roomRepository.deleteById(id);
    }

}
