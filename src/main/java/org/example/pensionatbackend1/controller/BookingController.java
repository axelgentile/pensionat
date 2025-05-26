package org.example.pensionatbackend1.controller;

import jakarta.validation.Valid;
import org.example.pensionatbackend1.Models.Room;
import org.example.pensionatbackend1.dto.*;
import org.example.pensionatbackend1.mapper.CustomerMapper;
import org.example.pensionatbackend1.mapper.RoomMapper;
import org.example.pensionatbackend1.service.BookingService;
import org.example.pensionatbackend1.service.CustomerService;
import org.example.pensionatbackend1.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private RoomService roomService;

    @GetMapping("/all")
    public String showAllBookings(Model model) {
        List<BookingDto> bookingDtos = bookingService.getAllBookings();
        model.addAttribute("bookings", bookingDtos);
        return "all-bookings";
    }

    @PostMapping("/delete/{id}")
    public String cancelBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookingService.deleteBooking(id);
            redirectAttributes.addFlashAttribute("bookingmessage", "Bokningen är avbokad");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Kunde inte avboka bokningen: " + e.getMessage());
        }
        return "redirect:/bookings/all";
    }

    @GetMapping("/search")
    public String showSearchForm(Model model) {
        model.addAttribute("roomSearchDto", new RoomSearchDto());
        return "search-rooms";
    }

    @GetMapping("/new")
    public String showBookingForm(Model model) {
        model.addAttribute("roomSearchDto", new RoomSearchDto());
        model.addAttribute("booking", new BookingDto());
        model.addAttribute("availableRooms", new ArrayList<>());
        return "search-rooms";
    }

    @PostMapping("/searchAvailableRooms")
    public String searchAvailableRooms(
            @Valid @ModelAttribute("roomSearchDto") RoomSearchDto roomSearchDto,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "search-rooms";
        }

        prepareAvailableRooms(model, roomSearchDto);
        return "available-rooms";
    }

    @PostMapping("/book")
    public String createBooking(@Valid @ModelAttribute BookingDto bookingDto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Formuläret är inte korrekt ifyllt.");
            return "available-rooms";
        }

        try {
            bookingService.createBooking(bookingDto);
            redirectAttributes.addFlashAttribute("successMessage", "Bokning skapad!");
            return "redirect:/bookings/all";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "available-rooms";
        }
    }

    @GetMapping("/edit/{bookingId}")
    public String showEditBookingForm(@PathVariable Long bookingId, Model model) {
        BookingDto bookingDto = bookingService.getBookingById(bookingId);
        RoomSearchDto roomSearchDto = new RoomSearchDto();

        roomSearchDto.setCheckIn(bookingDto.getCheckInDate());
        roomSearchDto.setCheckOut(bookingDto.getCheckOutDate());
        roomSearchDto.setGuests(bookingDto.getGuests());

        model.addAttribute("booking", bookingDto);
        model.addAttribute("roomSearchDto", roomSearchDto);
        return "edit-booking";
    }

    @PostMapping("/edit/{bookingId}/searchAvailableRooms")
    public String searchAvailableRoomsForEdit(
            @PathVariable Long bookingId,
            @Valid @ModelAttribute("roomSearchDto") RoomSearchDto roomSearchDto,
            BindingResult bindingResult,
            Model model) {

        BookingDto bookingDto = bookingService.getBookingById(bookingId);

        if (bindingResult.hasErrors()) {
            model.addAttribute("booking", bookingDto);
            return "edit-booking";
        }

        List<Room> availableRooms = getAvailableRoomsWithCurrent(bookingId, roomSearchDto);
        populateBookingModel(model, bookingDto, roomSearchDto, availableRooms);
        return "edit-booking";
    }

    @PostMapping("/edit/{bookingId}")
    public String updateBooking(
            @PathVariable Long bookingId,
            @RequestParam Long roomId,
            @ModelAttribute RoomSearchDto roomSearchDto,
            Model model) {

        BookingDto bookingDto = bookingService.getBookingById(bookingId);
        bookingDto.setRoomId(roomId);
        bookingDto.setCheckInDate(roomSearchDto.getCheckIn());
        bookingDto.setCheckOutDate(roomSearchDto.getCheckOut());
        bookingDto.setGuests(roomSearchDto.getGuests());

        try {
            bookingService.updateBooking(bookingId, bookingDto);
            model.addAttribute("successMessage", "Bokningen har uppdaterats.");
            return "redirect:/bookings/all";
        } catch (IllegalArgumentException e) {
            List<Room> rooms = bookingService.searchAvailableRooms(
                    roomSearchDto.getCheckIn(), roomSearchDto.getCheckOut(), roomSearchDto.getGuests());
            model.addAttribute("errorMessage", e.getMessage());
            populateBookingModel(model, bookingDto, roomSearchDto, rooms);
            return "edit-booking";
        }
    }


    private void prepareAvailableRooms(Model model, RoomSearchDto roomSearchDto) {
        List<RoomDto> roomDtos = bookingService.searchAvailableRooms(
                        roomSearchDto.getCheckIn(),
                        roomSearchDto.getCheckOut(),
                        roomSearchDto.getGuests()
                ).stream()
                .map(RoomMapper::toDto)
                .toList();

        List<CustomerDto> customers = customerService.getAllCustomers().stream()
                .map(CustomerMapper::toDto)
                .toList();

        model.addAttribute("availableRooms", roomDtos);
        model.addAttribute("customers", customers);
        model.addAttribute("checkIn", roomSearchDto.getCheckIn());
        model.addAttribute("checkOut", roomSearchDto.getCheckOut());
        model.addAttribute("guests", roomSearchDto.getGuests());
    }

    private List<Room> getAvailableRoomsWithCurrent(Long bookingId, RoomSearchDto searchDto) {
        List<Room> available = new ArrayList<>(bookingService.searchAvailableRooms(
                searchDto.getCheckIn(), searchDto.getCheckOut(), searchDto.getGuests()));

        Room currentRoom = roomService.getRoomById(bookingService.getBookingById(bookingId).getRoomId());
        if (currentRoom != null && !available.contains(currentRoom)) {
            available.add(currentRoom);
        }

        return available;
    }

    private void populateBookingModel(Model model, BookingDto bookingDto, RoomSearchDto roomSearchDto, List<Room> rooms) {
        model.addAttribute("booking", bookingDto);
        model.addAttribute("roomSearchDto", roomSearchDto);
        model.addAttribute("availableRooms", rooms.stream().map(RoomMapper::toDto).toList());
    }
}
