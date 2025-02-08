package com.vehicletrackingsys.api.repositories;

import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import com.vehicletrackingsys.api.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findById(UUID id);

}
