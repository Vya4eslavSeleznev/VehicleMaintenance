package com.vehicle.maintenance.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CarUpdateModel {

    private long id;
    private String brand;
    private String model;
    private String engine;
    private String color;
}
