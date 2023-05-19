package com.vehicle.maintenance.exception;

public class InvalidJwtAuthenticationException extends Exception {

    public InvalidJwtAuthenticationException() {
        super("Expired or invalid token");
    }
}
