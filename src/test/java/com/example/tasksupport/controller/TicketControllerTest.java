package com.example.tasksupport.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import com.example.tasksupport.service.TicketService;

import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.doNothing;

import static org.mockito.Mockito.when;

import com.example.tasksupport.exception.TicketNotFoundException;

import com.example.tasksupport.entity.Ticket;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.mockito.Mockito.verifyNoInteractions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@SpringBootTest
@AutoConfigureMockMvc
public class TicketControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private TicketService ticketService;

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void 存在しない問い合わせIDなら404になる() throws Exception {

                when(ticketService.findTicketById(999999))
                                .thenThrow(new TicketNotFoundException("問い合わせが見つかりません。"));

                mockMvc.perform(
                                get("/tickets/999999/edit"))
                                .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser(username = "user", roles = "USER")
        void 一般ユーザーは編集画面へアクセスできない() throws Exception {

                mockMvc.perform(
                                get("/tickets/1/edit"))
                                .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void 不正なID形式なら400になる() throws Exception {

                mockMvc.perform(
                                get("/tickets/abc/edit"))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(username = "user", roles = "USER")
        void 一般ユーザーは削除できない() throws Exception {

                mockMvc.perform(
                                post("/tickets/1/delete")
                                                .with(csrf()))
                                .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void 管理者は削除処理へアクセスできる() throws Exception {

                doNothing().when(ticketService).deleteTicket(1);

                mockMvc.perform(
                                post("/tickets/1/delete")
                                                .with(csrf()))
                                .andExpect(status().is3xxRedirection());
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void 管理者は編集画面へアクセスできる() throws Exception {

                Ticket ticket = new Ticket();

                when(ticketService.findTicketById(1))
                                .thenReturn(ticket);

                mockMvc.perform(
                                get("/tickets/1/edit"))
                                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void 編集時にタイトルが空欄なら更新されず編集画面を再表示する() throws Exception {

                mockMvc.perform(
                                post("/tickets/1/update")
                                                .with(csrf())
                                                .param("title", "")
                                                .param("priority", "高")
                                                .param("status", "未対応"))
                                .andExpect(status().isOk())
                                .andExpect(view().name("edit"));

                verifyNoInteractions(ticketService);
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void 管理者は詳細画面へアクセスできる() throws Exception {

                Ticket ticket = new Ticket();

                when(ticketService.findTicketById(49))
                                .thenReturn(ticket);

                mockMvc.perform(
                                get("/tickets/49/detail"))
                                .andExpect(status().isOk())
                                .andExpect(view().name("detail"))
                                .andExpect(model().attribute("ticket", ticket));
        }

        @Test
        @WithMockUser(username = "user", roles = "USER")
        void 一般ユーザーは詳細画面へアクセスできる() throws Exception {

                Ticket ticket = new Ticket();

                when(ticketService.findTicketById(49))
                                .thenReturn(ticket);

                mockMvc.perform(
                                get("/tickets/49/detail"))
                                .andExpect(status().isOk())
                                .andExpect(view().name("detail"));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void 詳細画面で存在しない問い合わせIDなら404になる() throws Exception {

                when(ticketService.findTicketById(999999))
                                .thenThrow(new TicketNotFoundException("問い合わせが見つかりません。"));

                mockMvc.perform(
                                get("/tickets/999999/detail"))
                                .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void 詳細画面へ一覧状態を引き継げる() throws Exception {

                Ticket ticket = new Ticket();

                when(ticketService.findTicketById(49))
                                .thenReturn(ticket);

                mockMvc.perform(
                                get("/tickets/49/detail")
                                                .param("page", "2")
                                                .param("title", "ログイン")
                                                .param("status", "未対応")
                                                .param("sort", "updatedDesc"))
                                .andExpect(status().isOk())
                                .andExpect(model().attribute("page", 2))
                                .andExpect(model().attribute("title", "ログイン"))
                                .andExpect(model().attribute("status", "未対応"))
                                .andExpect(model().attribute("sort", "updatedDesc"));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void 詳細画面でpage未指定なら0になる() throws Exception {

                Ticket ticket = new Ticket();

                when(ticketService.findTicketById(49))
                                .thenReturn(ticket);

                mockMvc.perform(
                                get("/tickets/49/detail"))
                                .andExpect(status().isOk())
                                .andExpect(model().attribute("page", 0));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void 一覧画面で並び順を指定したら値がServiceへ渡っている() throws Exception {

                when(ticketService.findTickets(
                                any(),
                                any(),
                                any(Pageable.class),
                                eq("updatedDesc")))
                                .thenReturn(Page.empty());

                mockMvc.perform(
                                get("/tickets")
                                                .param("sort", "updatedDesc"))
                                .andExpect(status().isOk());

                verify(ticketService).findTickets(
                                any(),
                                any(),
                                any(Pageable.class),
                                eq("updatedDesc"));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void 一覧画面でsort未指定なら登録日時の新しい順になる() throws Exception {

                when(ticketService.findTickets(
                                any(),
                                any(),
                                any(Pageable.class),
                                eq("registrationDesc")))
                                .thenReturn(Page.empty());

                mockMvc.perform(
                                get("/tickets"))
                                .andExpect(status().isOk());

                verify(ticketService).findTickets(
                                any(),
                                any(),
                                any(Pageable.class),
                                eq("registrationDesc"));
        }
}
