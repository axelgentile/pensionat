package org.example.pensionatbackend1.controller;
import org.example.pensionatbackend1.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/bookings")
@Controller
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/all")
    public String showAllBookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "all-bookings";
   }
    }
