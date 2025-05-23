package org.example.pensionatbackend1.Models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.pensionatbackend1.Models.modelenums.RoomType;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue
    private Long id;
    private Integer roomNumber;
    private RoomType roomType;
    private double pricePerNight;
    private int extraBeds;

    @OneToMany(mappedBy = "room")
    private List<Booking> bookings;

    public Room(Long id, Integer roomNumber, RoomType roomType, double pricePerNight, int extraBeds) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.extraBeds = extraBeds;
    }
}
