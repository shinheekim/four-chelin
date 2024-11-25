package com.example.fourchelin.common.template;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RspTemplate<T> {

    private final int statusCode;
    private final String message;
    private T data;

    public RspTemplate(HttpStatus httpStatus, String message, T data) {
        this.statusCode = httpStatus.value();
        this.message = message;
        this.data = data;
    }

    public RspTemplate(HttpStatus httpStatus, String message) {
        this.statusCode = httpStatus.value();
        this.message = message;
    }
}
