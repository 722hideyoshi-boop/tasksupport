package com.example.tasksupport.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.tasksupport.entity.Ticket;
import com.example.tasksupport.exception.TicketNotFoundException;
import com.example.tasksupport.repository.TicketRepository;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    void 存在するIDならTicketを返す() {

        Ticket ticket = new Ticket();

        when(ticketRepository.findById(1))
                .thenReturn(Optional.of(ticket));

        Ticket result = ticketService.findTicketById(1);

        assertEquals(ticket, result);
    }

    @Test
    void 存在しないIDならTicketNotFoundExceptionを投げる() {

        when(ticketRepository.findById(999999))
                .thenReturn(Optional.empty());

        assertThrows(
                TicketNotFoundException.class,
                () -> ticketService.findTicketById(999999));
    }
}