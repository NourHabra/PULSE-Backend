package com.pulse.exception;

public class ConsentAlreadyPendingException extends RuntimeException {
    public ConsentAlreadyPendingException(Long patientId) {
        super("Waiting for patient " + patientId + " to decide the previous request");
    }
}