package com.skywaet.vtbmarket.exception.notfound;

import com.skywaet.vtbmarket.exception.BaseException;

public abstract class BaseNotFoundException extends BaseException {
    public BaseNotFoundException(String entityName) {
        super(entityName + " not found.");
    }
}
