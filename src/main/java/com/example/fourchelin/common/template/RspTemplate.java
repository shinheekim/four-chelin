package com.example.fourchelin.common.template;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RspTemplate<T> {

    private final int statusCode;
    private String message; // 검색 응답에는 message가 포함되지 않기 때문에 final 제거
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

    public RspTemplate(HttpStatus httpStatus, T data ) {
        this.statusCode = httpStatus.value();
        this.data = data;
    }
}
