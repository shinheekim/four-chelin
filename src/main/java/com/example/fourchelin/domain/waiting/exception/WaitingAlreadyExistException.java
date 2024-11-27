package com.example.fourchelin.domain.waiting.exception;

public class WaitingAlreadyExistException extends RuntimeException {
    public WaitingAlreadyExistException(String message) {
        super(message);
    }
}
