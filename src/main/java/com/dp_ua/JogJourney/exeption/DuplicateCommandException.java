package com.dp_ua.JogJourney.exeption;

public class DuplicateCommandException extends RuntimeException {
    public DuplicateCommandException(String message) {
        super(message);
    }
}
