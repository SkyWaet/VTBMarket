package com.skywaet.vtbmarket.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class User extends BaseEntity {
    private String login;
    private String password;
    private String name;
    private String surname;

    private String address;
    private String phoneNumber;

    private List<Ticket> tickets = new ArrayList<>();
    private List<Purchase> purchases = new ArrayList<>();

    public User(Long id, String login, String password, String name, String surname, String address, String phoneNumber) {
        super(id);
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = Objects.isNull(tickets) ? new ArrayList<>() : tickets;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = Objects.isNull(purchases) ? new ArrayList<>() : purchases;
    }
}
