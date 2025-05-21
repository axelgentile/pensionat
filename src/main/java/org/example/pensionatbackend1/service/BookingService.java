package org.example.pensionatbackend1.service;

import org.example.pensionatbackend1.Models.Booking;
import org.example.pensionatbackend1.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    public List<Booking> getAllBookings(){
        return bookingRepository.findAll();
    }
}
