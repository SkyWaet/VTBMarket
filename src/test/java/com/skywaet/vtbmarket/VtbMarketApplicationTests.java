package com.skywaet.vtbmarket;

import com.skywaet.vtbmarket.dao.*;
import com.skywaet.vtbmarket.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class VtbMarketApplicationTests {
    public final User sampleUser = User.builder()
            .login("login")
            .password("pass")
            .name("name")
            .surname("surname")
            .address("address")
            .build();

    @Autowired
    public UserDao userDao;

    @Autowired
    public TicketDao ticketDao;

    @Autowired
    public PurchaseDao purchaseDao;

    @Autowired
    public ProductDao productDao;

    @Autowired
    public DeliveryOrderDao deliveryOrderDao;

    @Autowired
    public CategoryDao categoryDao;

}
