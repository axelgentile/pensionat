package org.example.pensionatbackend1.config;

import lombok.RequiredArgsConstructor;
import org.example.pensionatbackend1.Models.Room;
import org.example.pensionatbackend1.Models.modelenums.RoomType;
import org.example.pensionatbackend1.repository.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoomRepository roomRepository;

    @Override
    public void run(String... args) {
        if (roomRepository.count() == 0) {
            List<Room> rooms = Arrays.asList(
                    createRoom(101, RoomType.SINGLE, "895.00", 1),
                    createRoom(102, RoomType.DOUBLE, "1195.00", 2),
                    createRoom(103, RoomType.SINGLE, "895.00", 1),
                    createRoom(201, RoomType.DOUBLE, "1195.00", 2),
                    createRoom(202, RoomType.SINGLE, "895.00", 0),
                    createRoom(203, RoomType.DOUBLE, "1195.00", 2),
                    createRoom(301, RoomType.SINGLE, "995.00", 1),
                    createRoom(302, RoomType.DOUBLE, "1295.00", 2),
                    createRoom(303, RoomType.SINGLE, "895.00", 1),
                    createRoom(304, RoomType.DOUBLE, "1195.00", 2)
            );

            roomRepository.saveAll(rooms);
            System.out.println("Databasinitialiserare: " + rooms.size() + " rum har skapats!");
        } else {
            System.out.println("Databasinitialiserare: Databasen innehåller redan rum, hoppar över initialisering.");
        }
    }

    private Room createRoom(Integer roomNumber, RoomType roomType, String price, int extraBeds) {
        return new Room(
                null,
                roomNumber,
                roomType,
                new BigDecimal(price),
                extraBeds
        );
    }
}