package com.skywaet.vtbmarket.dao.impl;

import com.skywaet.vtbmarket.VtbMarketApplicationTests;
import com.skywaet.vtbmarket.exception.notfound.TicketNotFoundException;
import com.skywaet.vtbmarket.model.Ticket;
import com.skywaet.vtbmarket.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.skywaet.vtbmarket.helper.TestUtils.getTicket;

@SpringBootTest
class TicketJdbcDaoTest extends VtbMarketApplicationTests {

    @AfterEach
    void clearDb() {
        ticketDao.deleteAll();
        userDao.deleteAll();
    }

    @Test
    void testFindAll() {
        User user = userDao.create(sampleUser);
        for (int i = 0; i < 5; i++) {
            Ticket ticket = Ticket.builder()
                    .author(user)
                    .message("msg+i")
                    .priority(1)
                    .status("in work")
                    .build();
            ticketDao.create(ticket);
        }
        Assertions.assertEquals(5, ticketDao.findAll().size());
    }

    @Test
    void testCreateAndGet() {
        User user = userDao.create(sampleUser);
        Ticket ticket = getTicket();
        ticket.setAuthor(user);
        Ticket saved = ticketDao.create(ticket);
        Ticket fromDb = ticketDao.findById(saved.getId());
        Assertions.assertEquals(saved, fromDb);
    }

    @Test
    void testFindByNonExistentId() {
        Assertions.assertThrows(TicketNotFoundException.class, () -> ticketDao.findById(1000L));
    }


    @Test
    void testUpdate() {
        User user = userDao.create(sampleUser);
        Ticket ticket = getTicket();
        ticket.setAuthor(user);
        Ticket saved = ticketDao.create(ticket);
        saved.setPriority(6);
        Ticket updated = ticketDao.update(saved.getId(), saved);
        Ticket fromDb = ticketDao.findById(saved.getId());
        Assertions.assertEquals(updated, fromDb);
    }

    @Test
    void testUpdateByNonExistentId() {
        User user = userDao.create(sampleUser);
        Ticket ticket = getTicket();
        ticket.setAuthor(user);
        Assertions.assertThrows(TicketNotFoundException.class, () -> ticketDao.update(1000L, ticket));
    }

    @Test
    void testDelete() {
        User user = userDao.create(sampleUser);
        Ticket ticket = getTicket();
        ticket.setAuthor(user);
        Ticket saved = ticketDao.create(ticket);
        ticketDao.delete(saved.getId());
        Assertions.assertThrows(TicketNotFoundException.class, () -> ticketDao.findById(saved.getId()));
    }
}