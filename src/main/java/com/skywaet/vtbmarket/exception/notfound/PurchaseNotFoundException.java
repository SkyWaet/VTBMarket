package com.skywaet.vtbmarket.exception.notfound;

public class PurchaseNotFoundException extends BaseNotFoundException{
    public PurchaseNotFoundException(Long id) {
        super("Purchase with id="+id);
    }
}
