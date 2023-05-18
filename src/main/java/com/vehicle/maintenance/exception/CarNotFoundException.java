package com.vehicle.maintenance.exception;

public class CarNotFoundException extends Exception {

    public CarNotFoundException() {
        super("Car not found");
    }
}
