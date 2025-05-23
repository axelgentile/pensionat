package org.example.pensionatbackend1.repository;

import org.example.pensionatbackend1.Models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
      select b from Booking b
      where b.room.id = :roomId
        and b.checkInDate < :requestedCheckOut
        and b.checkOutDate > :requestedCheckIn
    """)
    List<Booking> findOverlapping(
            @Param("roomId") Long roomId,
            @Param("requestedCheckIn") LocalDate requestedCheckIn,
            @Param("requestedCheckOut") LocalDate requestedCheckOut);

    boolean existsByCustomerId(Long id);
}

