package com.example.fourchelin.common.exception;

import com.example.fourchelin.domain.member.exception.MemberException;
import com.example.fourchelin.domain.waiting.exception.WaitingAlreadyExistException;
import com.example.fourchelin.domain.waiting.exception.WaitingNotAuthorizedException;
import com.example.fourchelin.domain.waiting.exception.WaitingNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            MemberException.class,
            WaitingNotAuthorizedException.class
    })
    public ResponseEntity<Map<String, Object>> handleMemberException(Exception ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return getErrorResponse(status, ex.getMessage());
    }

    @ExceptionHandler(WaitingAlreadyExistException.class)
    public ResponseEntity<Map<String, Object>> handleWaitingAlreadyExistException(WaitingAlreadyExistException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return getErrorResponse(status, ex.getMessage());
    }

    @ExceptionHandler(WaitingNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(Exception ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return getErrorResponse(status, ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> getErrorResponse(HttpStatus status, String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status: ", status.name());
        errorResponse.put("code: ", status.value());
        errorResponse.put("message: ", message);

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
