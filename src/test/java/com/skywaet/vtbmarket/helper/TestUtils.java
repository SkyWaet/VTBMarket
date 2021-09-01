package com.skywaet.vtbmarket.helper;

import com.skywaet.vtbmarket.model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class TestUtils {
    public static Ticket getTicket() {
        return Ticket.builder()
                .message("msg")
                .priority(1)
                .status("in work")
                .build();
    }

    public static Purchase getPurchase() {
        return Purchase.builder()
                .status("status")
                .sumOfPayment(1000.0)
                .content(new ArrayList<>())
                .deliveryOrders(new ArrayList<>())
                .build();
    }

    public static Category getCategory() {
        return Category.builder().name("cat_name").build();
    }

    public static Product getProduct() {
        return Product.builder()
                .articleNumber("abc123")
                .price(10.0)
                .name("name")
                .build();
    }

    public static DeliveryOrder getDeliveryOrder() {
        return DeliveryOrder.builder()
                .addressee("addressee")
                .status("ok")
                .dateTime(LocalDateTime.now())
                .location("location")
                .contactNumber("number")
                .build();
    }
}
