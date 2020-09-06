package com.ple.example.icommerce.exp;

import com.ple.example.icommerce.dto.error.ErrorItem;
import com.ple.example.icommerce.dto.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException exception) {
        log.debug("Handle MethodArgumentNotValidException ...");
        ErrorResponse errors = new ErrorResponse();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            ErrorItem errorItem = new ErrorItem();
            errorItem.setCode(((FieldError) error).getField());
            errorItem.setMessage(error.getDefaultMessage());
            errors.addError(errorItem);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handle(ConstraintViolationException exception) {
        log.debug("Handle ConstraintViolationException ...");
        ErrorResponse errors = new ErrorResponse();
        exception.getConstraintViolations().forEach(violation -> {
            ErrorItem error = new ErrorItem();
            error.setCode(violation.getMessageTemplate());
            error.setMessage(violation.getMessage());
            errors.addError(error);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
