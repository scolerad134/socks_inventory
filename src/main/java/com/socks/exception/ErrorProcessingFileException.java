package com.socks.exception;

public class ErrorProcessingFileException extends RuntimeException {

    public ErrorProcessingFileException(String message) {

        super(message);
    }

    public ErrorProcessingFileException(String message, Throwable cause) {

        super(message, cause);
    }
}
