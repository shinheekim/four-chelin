package com.example.fourchelin.common.exception;

import com.example.fourchelin.domain.member.exception.MemberException;
import com.example.fourchelin.domain.search.exception.SearchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<Map<String, Object>> handleMemberException(MemberException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return getErrorResponse(status, ex.getMessage());
    }

    @ExceptionHandler(SearchException.class)
    public ResponseEntity<Map<String, Object>> handleSearchException(SearchException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return getErrorResponse(status, ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> getErrorResponse(HttpStatus status, String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("staus: ", status.name());
        errorResponse.put("code: ", status.value());
        errorResponse.put("message: ", message);

        return new ResponseEntity<>(errorResponse, status);
    }
}
