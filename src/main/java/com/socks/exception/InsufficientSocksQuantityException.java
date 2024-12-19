package com.socks.exception;

public class InsufficientSocksQuantityException extends RuntimeException {

    public InsufficientSocksQuantityException(String message) {

        super(message);
    }

    public InsufficientSocksQuantityException(String message, Throwable cause) {

        super(message, cause);
    }
}
