package com.example.tasksupport.repository;

import com.example.tasksupport.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    
}
