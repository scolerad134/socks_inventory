package com.socks.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {
        InsufficientSocksQuantityException.class,
        NoSuchSocksException.class,
        IncorrectDataFormatException.class})
    public ResponseEntity<Object> handleSocksException(RuntimeException e) {
        ApiException apiException = new ApiException(
            e.getMessage(),
            e,
            HttpStatus.BAD_REQUEST,
            ZonedDateTime.now()
        );

        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ErrorProcessingFileException.class})
    public ResponseEntity<Object> handleErrorProcessingFile(ErrorProcessingFileException e) {

        ApiException apiException = new ApiException(
            e.getMessage(),
            e,
            HttpStatus.INTERNAL_SERVER_ERROR,
            ZonedDateTime.now()
        );

        return new ResponseEntity<>(apiException, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
