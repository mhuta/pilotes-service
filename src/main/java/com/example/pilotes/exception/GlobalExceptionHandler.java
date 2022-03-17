package com.example.pilotes.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({IllegalArgumentException.class})
  public final ResponseEntity<String> handleException(Exception ex) {
    HttpHeaders headers = new HttpHeaders();
    HttpStatus status = HttpStatus.BAD_REQUEST;
    return new ResponseEntity<>("Invalid data was provided! Message: " + ex.getMessage(), headers, status);
  }
}
