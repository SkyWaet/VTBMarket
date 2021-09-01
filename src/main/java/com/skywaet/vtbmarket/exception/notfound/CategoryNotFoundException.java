package com.skywaet.vtbmarket.exception.notfound;

public class CategoryNotFoundException extends BaseNotFoundException {
    public CategoryNotFoundException(Long id) {
        super("Category with id=" + id);
    }
}
