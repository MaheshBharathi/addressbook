package com.pwc.addressbook.exception;

import org.springframework.validation.Errors;

import static java.util.stream.Collectors.joining;

public class ContactValidationException extends RuntimeException {

    public ContactValidationException() {
        super();
    }

    public ContactValidationException(final String message) {
        super(message);
    }

    public ContactValidationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ContactValidationException(final Throwable cause) {
        super(cause);
    }

    public ContactValidationException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ContactValidationException(final Errors errors) {
        super(getErrors(errors));
    }

    private static String getErrors(final Errors errors) {
        return errors.getFieldErrors()
                .stream()
                .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
                .collect(joining(", "));
    }
}
