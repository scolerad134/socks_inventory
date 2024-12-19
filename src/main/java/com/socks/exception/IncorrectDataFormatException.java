package com.socks.exception;

public class IncorrectDataFormatException extends RuntimeException {

    public IncorrectDataFormatException(String message) {

        super(message);
    }

    public IncorrectDataFormatException(String message, Throwable cause) {

        super(message, cause);
    }
}
