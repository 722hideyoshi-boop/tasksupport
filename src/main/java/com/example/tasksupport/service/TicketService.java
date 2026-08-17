package com.example.tasksupport.service;

import org.springframework.stereotype.Service;

import com.example.tasksupport.repository.TicketRepository;
import com.example.tasksupport.entity.Ticket;

import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Page;

import com.example.tasksupport.exception.TicketNotFoundException;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Page<Ticket> findTickets(String title, String status, Pageable pageable) {

        boolean hasTitle = title != null && !title.isEmpty();
        boolean hasStatus = status != null && !status.isEmpty();

        if (hasTitle && hasStatus) {
            return ticketRepository.findByTitleContainingAndStatus(title, status, pageable);
        }

        if (hasTitle) {
            return ticketRepository.findByTitleContaining(title, pageable);
        }

        if (hasStatus) {
            return ticketRepository.findByStatus(status, pageable);
        }

        return ticketRepository.findAll(pageable);
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

        return ticketRepository.findById(id).orElseThrow(() -> new TicketNotFoundException("問い合わせが見つかりません。"));
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
        Ticket ticket = findTicketById(id);
        ticketRepository.delete(ticket);

    }
}