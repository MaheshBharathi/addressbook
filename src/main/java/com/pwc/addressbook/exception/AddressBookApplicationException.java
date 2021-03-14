package com.pwc.addressbook.exception;

public class AddressBookApplicationException extends RuntimeException {

    public AddressBookApplicationException() {
        super();
    }

    public AddressBookApplicationException(final String message) {
        super(message);
    }

    public AddressBookApplicationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AddressBookApplicationException(final Throwable cause) {
        super(cause);
    }

    public AddressBookApplicationException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
