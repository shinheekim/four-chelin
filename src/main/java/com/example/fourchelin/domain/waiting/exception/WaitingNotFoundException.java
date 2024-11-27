package com.example.fourchelin.domain.waiting.exception;

public class WaitingNotFoundException extends RuntimeException {
    public WaitingNotFoundException(String message) {
        super(message);
    }
}
