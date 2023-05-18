package com.vehicle.maintenance.exception;

public class MaintenanceNotFoundException extends Exception {

    public MaintenanceNotFoundException() {
        super("Maintenance not found");
    }
}
