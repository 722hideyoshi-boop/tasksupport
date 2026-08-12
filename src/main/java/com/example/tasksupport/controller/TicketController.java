package com.example.tasksupport.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import com.example.tasksupport.entity.Ticket;
import com.example.tasksupport.service.TicketService;

import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.PathVariable;

import com.example.tasksupport.form.TicketRegisterForm;

import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Page;

import org.springframework.data.web.PageableDefault;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public String index(
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "title", required = false) String title, 
            @PageableDefault(size = 10) Pageable pageable,
            Model model) {

        Page<Ticket> tickets = ticketService.findTickets(title, status, pageable);
        model.addAttribute("tickets", tickets);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("title", title);

        return "index";
    }

    @GetMapping("/new")
    public String newTicket(Model model) {

        model.addAttribute(
                "ticketRegisterForm",
                new TicketRegisterForm());

        return "new";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Integer id, Model model) {
        Ticket ticket = ticketService.findTicketById(id);

        model.addAttribute("ticket", ticket);

        return "edit";
    }

    @PostMapping
    public String create(@Validated TicketRegisterForm ticketRegisterForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            return "new";

        }

        ticketService.createTicket(
                ticketRegisterForm.getTitle(),
                ticketRegisterForm.getPriority());

        return "redirect:/tickets";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Integer id, @RequestParam String title, @RequestParam String priority,
            @RequestParam String status) {

        ticketService.updateTicket(id, title, priority, status);

        return "redirect:/tickets";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {

        ticketService.deleteTicket(id);

        return "redirect:/tickets";
    }
}
