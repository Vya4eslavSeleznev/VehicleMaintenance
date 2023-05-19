package com.vehicle.maintenance.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
public class CarFindModel {

    private long id;
    private String brand;
    private String model;
    private String engine;
    private String color;
    private String name;
    private String surname;
    private String lastName;
    private String phone;
    private Date birthDate;
}
