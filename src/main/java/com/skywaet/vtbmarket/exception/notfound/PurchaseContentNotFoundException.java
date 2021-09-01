package com.skywaet.vtbmarket.exception.notfound;

public class PurchaseContentNotFoundException extends BaseNotFoundException{

    public PurchaseContentNotFoundException(Long id) {
        super("Purchase content with id="+id);
    }
}
