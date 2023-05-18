package com.vehicle.maintenance.repository;

import com.vehicle.maintenance.entity.Maintenance;
import org.springframework.data.repository.CrudRepository;

public interface MaintenanceRepository extends CrudRepository<Maintenance, Long> {
}
