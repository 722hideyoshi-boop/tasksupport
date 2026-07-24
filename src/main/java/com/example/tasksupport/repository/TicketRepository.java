package com.example.tasksupport.repository;

import com.example.tasksupport.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    public List<Ticket> findByStatus(String status);
}
