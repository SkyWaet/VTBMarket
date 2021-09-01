package com.skywaet.vtbmarket.exception.notfound;

public class TicketNotFoundException extends BaseNotFoundException {
    public TicketNotFoundException(Long id) {
        super("Ticket with id=" + id);
    }
}
