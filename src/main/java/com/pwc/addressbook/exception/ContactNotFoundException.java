package com.pwc.addressbook.exception;

public class ContactNotFoundException extends RuntimeException {

    public ContactNotFoundException() {
        super();
    }

    public ContactNotFoundException(final String message) {
        super(message);
    }

    public ContactNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ContactNotFoundException(final Throwable cause) {
        super(cause);
    }

    public ContactNotFoundException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
