package com.pulse.exception;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException() {
        super("Invalid credentials: The password is incorrect.");
    }
}
