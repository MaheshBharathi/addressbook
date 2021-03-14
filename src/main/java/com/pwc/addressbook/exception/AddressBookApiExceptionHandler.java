package com.pwc.addressbook.exception;

import com.pwc.addressbook.model.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class AddressBookApiExceptionHandler {

    @ExceptionHandler(ContactExistException.class)
    public ResponseEntity<ErrorResponse> contactExistException(ContactExistException ex) {
        ErrorResponse error = new ErrorResponse("Validation Error", 409, String.format("Contact %s already exist", ex.getMessage()));
        return new ResponseEntity<>(error, CONFLICT);
    }

    @ExceptionHandler(ContactNotFoundException.class)
    public ResponseEntity<ErrorResponse> contactNotFoundException(ContactNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("Validation Error", 404, String.format("Contact %s not found", ex.getMessage()));
        return new ResponseEntity<>(error, CONFLICT);
    }

    @ExceptionHandler(ContactValidationException.class)
    public ResponseEntity<ErrorResponse> contactValidationException(ContactValidationException ex) {
        ErrorResponse error = new ErrorResponse(BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleRootException(Exception ex) {
        ErrorResponse error = new ErrorResponse(INTERNAL_SERVER_ERROR.value(), "Ops! Something went wrong. Please try again later");
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(error);
    }
}
