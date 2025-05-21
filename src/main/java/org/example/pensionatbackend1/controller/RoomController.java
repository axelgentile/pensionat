package org.example.pensionatbackend1.controller;

import org.example.pensionatbackend1.Models.Room;
import org.example.pensionatbackend1.dto.RoomDto;
import org.example.pensionatbackend1.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/all")
    public String showAllRooms(Model model) {
        List<RoomDto> rooms = roomService.getAllRoomDtos();
        model.addAttribute("rooms", rooms);
        return "rooms";
    }
    @GetMapping("/create")
    public String showCreateRoomForm(Model model) {
        model.addAttribute("room", new Room());
        return "room-form";
    }

    @PostMapping("create")
    public String createRoom(@ModelAttribute Room room, RedirectAttributes redirectAttributes) {
        try {
            roomService.createRoom(room);
            redirectAttributes.addFlashAttribute("successMessage", "Rummet har skapats.");
            return "redirect:/rooms/all";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errormessage", e.getMessage());
            return "redirect:/rooms/create";
        }
    }

}





