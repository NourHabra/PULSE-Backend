package com.pulse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Object> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409
                .body(new ErrorResponse("EmailAlreadyExists", ex.getMessage()));
    }

    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<Object> handleWrongPassword(WrongPasswordException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN) // 403
                .body(new ErrorResponse("WrongPassword", ex.getMessage()));
    }


    public static class ErrorResponse {
        private String error;
        private String message;

        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
        }

        public String getError() {
            return error;
        }

        public String getMessage() {
            return message;
        }
    }
}
