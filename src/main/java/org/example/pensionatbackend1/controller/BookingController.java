package org.example.pensionatbackend1.controller;

import org.example.pensionatbackend1.dto.BookingDto;
import org.example.pensionatbackend1.mapper.BookingMapper;
import org.example.pensionatbackend1.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @RequestMapping("/all")
    public String showAllBookings(Model model){
        List<BookingDto> bookings = bookingService.getAllBookings().stream().map(BookingMapper::toDto).toList();
        model.addAttribute("bookings", bookings);
        return "bookings";
    }
}
