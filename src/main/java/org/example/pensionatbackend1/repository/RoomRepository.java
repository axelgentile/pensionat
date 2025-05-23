package org.example.pensionatbackend1.repository;
import org.example.pensionatbackend1.Models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByRoomNumber (Integer roomNumber);
}
