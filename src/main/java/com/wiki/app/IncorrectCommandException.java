package com.wiki.app;

public class IncorrectCommandException extends RuntimeException {

    public IncorrectCommandException() {
    }

    public IncorrectCommandException(String message) {
        super(message);
    }

    public IncorrectCommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectCommandException(Throwable cause) {
        super(cause);
    }

    public IncorrectCommandException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
