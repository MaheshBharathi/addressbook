package com.pwc.addressbook.exception;

public class ContactExistException extends RuntimeException {

    public ContactExistException() {
        super();
    }

    public ContactExistException(final String message) {
        super(message);
    }

    public ContactExistException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ContactExistException(final Throwable cause) {
        super(cause);
    }

    public ContactExistException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
