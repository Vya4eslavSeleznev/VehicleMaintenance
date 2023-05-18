package com.vehicle.maintenance.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
public class CustomerFindModel {

    private long id;
    private String name;
    private String surname;
    private String lastName;
    private String phone;
    private Date birthDate;
    private String username;
}
