package com.vehicletrackingsys.api.repositories;

import com.vehicletrackingsys.api.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
    List<Vehicle> findByOwnerEmail(String email);
    Optional<Vehicle> findByLicensePlate(String licensePlate);

    @Query("SELECT v FROM Vehicle v WHERE v.owner.email = :email AND v.id NOT IN (SELECT d.assignedVehicleId.id FROM Driver d WHERE d.assignedVehicleId IS NOT NULL)")
    List<Vehicle> findUnassignedVehiclesByOwnerEmail(@Param("email") String email);

}
