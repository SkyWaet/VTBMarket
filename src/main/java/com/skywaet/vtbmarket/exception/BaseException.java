package com.skywaet.vtbmarket.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class BaseException extends RuntimeException{
    private final String message;

}
