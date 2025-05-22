package org.example.pensionatbackend1.controller;
import org.example.pensionatbackend1.dto.BookingDto;
import org.example.pensionatbackend1.mapper.BookingMapper;
import org.example.pensionatbackend1.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/bookings")
@Controller
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/all")
    public String showAllBookings(Model model) {
        List<BookingDto> bookingDtos = bookingService.getAllBookings(); // returnerar redan DTOs
        model.addAttribute("bookings", bookingDtos);
        return "all-bookings";
    }
   @PostMapping("/delete/{id}")
    public String cancelBooking(@PathVariable Long id, RedirectAttributes redirectAttributes){
        try{
            bookingService.deleteBooking(id);
            redirectAttributes.addFlashAttribute("message", "Boknigen är avbokad");
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", "Kunde inte avboka bokningen: " + e.getMessage());
        }
        return "redirect:/bookings/all";
   }
   @GetMapping("/bookroom")
    public String bookRoom( @RequestParam("checkIn") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
                            @RequestParam("checkOut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
                            @RequestParam("guests") int guests,
                            Model model){

   }
}
