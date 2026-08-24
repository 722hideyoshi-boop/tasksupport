package com.example.tasksupport.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import com.example.tasksupport.entity.Ticket;
import com.example.tasksupport.exception.TicketNotFoundException;
import com.example.tasksupport.repository.TicketRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.verify;

import org.springframework.data.domain.Sort;

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

    @Test
    void 最終更新日時の新しい順を指定できる() {

        // ① 偽物Repositoryの準備
        // findAll(Pageable) が呼ばれたら、空のPageを返す
        when(ticketRepository.findAll(
                any(Pageable.class)))
                .thenReturn(Page.empty());

        // ② Serviceへ渡すPageableを作る
        // page番号 = 2
        // 1ページあたりの件数 = 10
        Pageable pageable = PageRequest.of(2, 10);

        // ③ 本物のTicketService.findTickets()を実行
        // title = null
        // status = null
        // pageable = ②で作ったもの
        // sort = "updatedDesc"
        ticketService.findTickets(
                null,
                null,
                pageable,
                "updatedDesc");

        // ④ sortedPageableを捕まえるための捕獲係を作る
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);

        // ⑤ RepositoryのfindAll()へ実際に渡されたPageableを捕まえる
        verify(ticketRepository)
                .findAll(captor.capture());

        // ⑥ captorが捕まえたPageableを取り出して普通の変数に入れる
        Pageable actualPageable = captor.getValue();

        // ⑦ Serviceがページ番号を維持できているか確認
        assertEquals(
                2,
                actualPageable.getPageNumber());

        // ⑧ 1ページあたりの件数が維持されているか確認
        assertEquals(
                10,
                actualPageable.getPageSize());

        // ⑨-1 Repositoryへ渡されたPageableからSortを取り出す
        Sort actualSort = actualPageable.getSort();

        // ⑨-2 updatedDateの並び替え条件をSortから取り出す
        Sort.Order order = actualSort.getOrderFor("updatedDate");

        // ⑨-3 並び替え対象がupdatedDateになっているか確認
        assertEquals(
                "updatedDate",
                order.getProperty());

        // ⑩ 並び順が新しい順(DESC)になっているか確認
        assertEquals(
                Sort.Direction.DESC,
                order.getDirection());

    }

    @Test
    void 登録日時の古い順を指定できる() {

        when(ticketRepository.findAll(
                any(Pageable.class)))
                .thenReturn(Page.empty());

        Pageable pageable = PageRequest.of(2, 10);

        ticketService.findTickets(null,
                null,
                pageable,
                "registrationAsc");

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);

        verify(ticketRepository)
                .findAll(captor.capture());

        Pageable actualPageable = captor.getValue();

        assertEquals(
                2,
                actualPageable.getPageNumber());

        assertEquals(
                10,
                actualPageable.getPageSize());

        Sort actualSort = actualPageable.getSort();

        Sort.Order order = actualSort.getOrderFor("registrationDate");

        assertEquals("registrationDate",
                order.getProperty());

        assertEquals(
                Sort.Direction.ASC,
                order.getDirection());
    }
}
