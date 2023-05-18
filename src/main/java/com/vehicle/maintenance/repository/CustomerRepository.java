package com.vehicle.maintenance.repository;

import com.vehicle.maintenance.entity.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
