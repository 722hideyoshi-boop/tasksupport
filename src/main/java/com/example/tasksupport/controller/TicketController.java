package com.example.tasksupport.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import com.example.tasksupport.entity.Ticket;
import com.example.tasksupport.repository.TicketRepository;

import java.util.List;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final TicketRepository ticketRepository;
    public TicketController(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @GetMapping
    public String index(Model model) {
        List<Ticket> tickets = ticketRepository.findAll();
        model.addAttribute("tickets", tickets);
        return "index";
    }

}
