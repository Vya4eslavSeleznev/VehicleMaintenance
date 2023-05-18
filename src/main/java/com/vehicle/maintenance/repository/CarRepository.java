package com.vehicle.maintenance.repository;

import com.vehicle.maintenance.entity.Car;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CarRepository extends CrudRepository<Car, Long> {

    void deleteByIdIn(List<Long> ids);
    List<Car> findByCustomerId(long customerId);
}
