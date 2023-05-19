package com.vehicle.maintenance.exception;

public class InvalidUserNameOrPasswordException extends Exception {

    public InvalidUserNameOrPasswordException() {
        super("Invalid user credentials");
    }
}
