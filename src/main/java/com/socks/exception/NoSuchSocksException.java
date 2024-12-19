package com.socks.exception;

public class NoSuchSocksException extends RuntimeException {

    public NoSuchSocksException(String message) {

        super(message);
    }

    public NoSuchSocksException(String message, Throwable cause) {

        super(message, cause);
    }
}
