package com.skywaet.vtbmarket.exception.notfound;

public class UserNotFoundException extends BaseNotFoundException {
    public UserNotFoundException(Long id) {
        super("User with id=" + id);
    }
}
