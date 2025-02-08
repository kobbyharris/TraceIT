package com.vehicletrackingsys.api.repositories;

import com.vehicletrackingsys.api.models.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DriverRepository extends JpaRepository<Driver, UUID> {
    List<Driver> findByOwnerId(UUID ownerId);
    boolean existsByOwnerId(UUID ownerId);
}
