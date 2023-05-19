package com.vehicle.maintenance.repository;

import com.vehicle.maintenance.entity.Credential;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CredentialRepository extends CrudRepository<Credential, Long> {

    Optional<Credential> findUserCredentialByUsername(String username);
}
