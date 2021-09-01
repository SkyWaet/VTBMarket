package com.skywaet.vtbmarket.exception.notfound;

public class DeliveryOrderNotFoundException extends BaseNotFoundException {
    public DeliveryOrderNotFoundException(Long id) {
        super("Delivery order with id=" + id);
    }
}
