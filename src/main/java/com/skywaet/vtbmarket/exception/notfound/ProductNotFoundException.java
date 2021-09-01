package com.skywaet.vtbmarket.exception.notfound;

public class ProductNotFoundException extends BaseNotFoundException{
    public ProductNotFoundException(Long id) {
        super("Product with id="+id);
    }
}
