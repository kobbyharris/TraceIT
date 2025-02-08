package com.vehicletrackingsys.api.repositories;

import com.vehicletrackingsys.api.models.Tracking;
import com.vehicletrackingsys.api.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrackingRepository extends JpaRepository<Tracking, UUID> {
    List<Tracking> findByVehicleIn(List<Vehicle> vehicles);
    List<Tracking> findByVehicleLicensePlate(String licensePlate);
    void deleteByVehicleId(UUID vehicleId);
    Optional<Tracking> findByGpsIMEI(String gpsIMEI);
}
