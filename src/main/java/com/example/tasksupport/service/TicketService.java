package com.example.tasksupport.service;

import org.springframework.stereotype.Service;

import com.example.tasksupport.repository.TicketRepository;
import com.example.tasksupport.entity.Ticket;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Ticket> findTickets(String title, String status) {

        boolean hasTitle = title != null && !title.isEmpty();
        boolean hasStatus = status != null && !status.isEmpty();

        if (hasTitle && hasStatus) {
            return ticketRepository.findByTitleContainingAndStatus(title, status);
        }

        if (hasTitle) {
            return ticketRepository.findByTitleContaining(title);
        }

        if (hasStatus) {
            return ticketRepository.findByStatus(status);
        }

        return ticketRepository.findAll();
    }

    public void createTicket(String title, String priority) {

        Ticket ticket = new Ticket();

        ticket.setTitle(title);
        ticket.setPriority(priority);
        ticket.setStatus("未対応");
        ticket.setRegistrationDate(LocalDateTime.now());
        ticket.setUpdatedDate(LocalDateTime.now());

        ticketRepository.save(ticket);
    }

    public Ticket findTicketById(Integer id) {

        return ticketRepository.findById(id).orElseThrow();
    }

    public void updateTicket(Integer id, String title, String priority, String status) {
        Ticket ticket = findTicketById(id);
        ticket.setTitle(title);
        ticket.setPriority(priority);
        ticket.setStatus(status);
        ticket.setUpdatedDate(LocalDateTime.now());

        ticketRepository.save(ticket);
    }

    public void deleteTicket(Integer id) {

        ticketRepository.deleteById(id);

    }
}