package com.pulse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;

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

    @ExceptionHandler(ConsentAlreadyPendingException.class)
    public ResponseEntity<String> handlePending(ConsentAlreadyPendingException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)        // 409
                .body(ex.getMessage());             // "Waiting for patient …"
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("Forbidden",
                        ex.getMessage() != null ? ex.getMessage() : "You are not authorized to see this data"));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatus(ResponseStatusException ex) {
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(new ErrorResponse("Forbidden",
                        ex.getReason() != null ? ex.getReason() : "You are not authorized to see this data"));
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
