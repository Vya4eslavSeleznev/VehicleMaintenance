package com.vehicle.maintenance.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "car")
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    public Car(String brand, String model, String engine, String color, Customer customer) {
        this.brand = brand;
        this.model = model;
        this.engine = engine;
        this.color = color;
        this.customer = customer;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String engine;

    @Column(nullable = false)
    private String color;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
