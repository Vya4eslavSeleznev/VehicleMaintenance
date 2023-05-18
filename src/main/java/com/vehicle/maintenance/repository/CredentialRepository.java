package com.vehicle.maintenance.repository;

import com.vehicle.maintenance.entity.Credential;
import org.springframework.data.repository.CrudRepository;

public interface CredentialRepository extends CrudRepository<Credential, Long> {
}
