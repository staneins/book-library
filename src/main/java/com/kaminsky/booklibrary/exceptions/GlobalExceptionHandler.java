package com.kaminsky.booklibrary.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>("Некорректные данные в запросе", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleInvalidJson(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>("Невалидный JSON", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleInvalidMethod(HttpRequestMethodNotSupportedException ex) {
        return new ResponseEntity<>("Такой тип запроса не поддерживается", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonException.class)
    public ResponseEntity<String> handleInvalidMethod(JsonException ex) {
        return new ResponseEntity<>("Проблема с JSON", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleInvalidMethod(ResponseStatusException ex) {
        return new ResponseEntity<>("Объект по запросу не найден", HttpStatus.NOT_FOUND);
    }
}
