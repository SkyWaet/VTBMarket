package com.skywaet.vtbmarket.exception;

public class UnknownException extends BaseException {
    public UnknownException(String message) {
        super("Unknown error: " + message);
    }
}
