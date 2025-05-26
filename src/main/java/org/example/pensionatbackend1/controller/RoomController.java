package org.example.pensionatbackend1.controller;

import org.example.pensionatbackend1.dto.RoomDto;
import org.example.pensionatbackend1.mapper.RoomMapper;
import org.example.pensionatbackend1.Models.Room;
import org.example.pensionatbackend1.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
        List<RoomDto> rooms = roomService.getAllRooms().stream().map(RoomMapper::toDto).toList();
        model.addAttribute("rooms", rooms);
        return "rooms";
    }

    @GetMapping("/create")
    public String showCreateRoomForm(Model model) {
        model.addAttribute("roomDto", new RoomDto());
        return "room-form";
    }

    @PostMapping("/create")
    public String createRoom(@ModelAttribute("roomDto")RoomDto roomDto, RedirectAttributes redirectAttributes) {
        try {
            Room room = RoomMapper.toEntity(roomDto);
            roomService.createRoom(room);
            redirectAttributes.addFlashAttribute("successMessage", "Rummet har skapats.");
            return "redirect:/rooms/all";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errormessage", e.getMessage());
            return "redirect:/rooms/create";
        }
    }
    @PostMapping("/delete/{id}")
    public String deleteRoom(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (roomService.getRoomById(id) == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Rummet finns inte.");
            return "redirect:/rooms/all";
        }
        try {
            roomService.deleteRoomById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Rummet har tagits bort.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Rummet kan inte tas bort eftersom det är bokat av en eller flera kunder.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Ett fel uppstod vid borttagning av rummet.");
        }

        return "redirect:/rooms/all";
    }

}
