package com.socks.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientSocksQuantityException extends RuntimeException {
    public InsufficientSocksQuantityException(String message) {
        super(message);
    }
}
