package com.vehicle.maintenance.repository;

import com.vehicle.maintenance.entity.Maintenance;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MaintenanceRepository extends CrudRepository<Maintenance, Long> {

    List<Maintenance> findByCarIdIn(List<Long> carId);
    List<Maintenance> findByCarId(long carId);
    void deleteByIdIn(List<Long> ids);
}
