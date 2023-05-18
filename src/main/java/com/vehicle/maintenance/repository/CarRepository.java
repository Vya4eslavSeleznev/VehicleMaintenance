package com.vehicle.maintenance.repository;

import com.vehicle.maintenance.entity.Car;
import org.springframework.data.repository.CrudRepository;

public interface CarRepository extends CrudRepository<Car, Long> {
}
