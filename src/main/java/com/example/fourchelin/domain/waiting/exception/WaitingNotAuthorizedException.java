package com.example.fourchelin.domain.waiting.exception;

public class WaitingNotAuthorizedException extends RuntimeException {
    public WaitingNotAuthorizedException(String message) {
        super(message);
    }
}
