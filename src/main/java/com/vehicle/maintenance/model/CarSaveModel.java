package com.vehicle.maintenance.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CarSaveModel {

    private String brand;
    private String model;
    private String engine;
    private String color;
    private long customerId;
}
