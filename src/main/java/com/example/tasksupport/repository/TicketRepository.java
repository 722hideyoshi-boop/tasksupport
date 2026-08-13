package com.example.tasksupport.repository;

import com.example.tasksupport.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    public Page<Ticket> findByStatus(String status, Pageable pageable);

    public Page<Ticket> findByTitleContaining(String title, Pageable pageable);

    public Page<Ticket> findByTitleContainingAndStatus(String title, String status, Pageable pageable);

}
