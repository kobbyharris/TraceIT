package com.vehicletrackingsys.api.services;

import com.vehicletrackingsys.api.dtos.DriverDTO;
import com.vehicletrackingsys.api.models.Driver;
import com.vehicletrackingsys.api.models.User;
import com.vehicletrackingsys.api.models.Vehicle;
import com.vehicletrackingsys.api.repositories.DriverRepository;
import com.vehicletrackingsys.api.repositories.UserRepository;
import com.vehicletrackingsys.api.repositories.VehicleRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    public boolean userHasDriversById(UUID id) {
        return driverRepository.existsByOwnerId(id);
    }

    // Get all drivers for the user by email
    public List<DriverDTO> getDriversByOwnerId(UUID id) {
        List<Driver> drivers = driverRepository.findByOwnerId(id);
        return drivers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    /*Can use this method and shuffle items from database

    public List<DriverDTO> getRandomDriversByOwnerId(UUID ownerId) {
        List<Driver> drivers = driverRepository.findByOwnerId(ownerId);

        Collections.shuffle(drivers);

        return drivers.stream()
                .limit(6)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }*/

    // Get driver by ID
    public DriverDTO getDriverById(UUID id) {
        Optional<Driver> driver = driverRepository.findById(id);
        return driver.map(this::convertToDTO).orElse(null);
    }

    // Create a new driver
    @Transactional
    public void createDriver(DriverDTO driverDTO, HttpSession session) {
        Optional<User> owner = userRepository.findById((UUID) session.getAttribute("userId"));

        if (owner.isEmpty()) {
            throw new IllegalArgumentException("Owner with this id does not exist");
        }

        Vehicle assignedVehicle = null;

        // Check if the driver has an assigned vehicle
        if (driverDTO.getAssignedVehicleId() != null) {
            assignedVehicle = vehicleRepository.findById(driverDTO.getAssignedVehicleId())
                    .orElseThrow(() -> new IllegalArgumentException("Vehicle with the provided id does not exist"));
        }

        Driver driver = convertToEntity(driverDTO, owner.get(), assignedVehicle);

        // Save the driver
        driverRepository.save(driver);
    }




    // Update an existing driver
    public DriverDTO updateDriver(DriverDTO driverDTO) {
        // Find the existing driver by ID
        Optional<Driver> existingDriver = driverRepository.findById(driverDTO.getId());

        // If the driver doesn't exist, handle it appropriately
        if (existingDriver.isEmpty()) {
            throw new IllegalArgumentException("Driver with the provided ID does not exist");
        }

        // Convert DTO to entity and update only the necessary fields
        Driver driverToUpdate = existingDriver.get();
        driverToUpdate.setFullName(driverDTO.getFullName());
        driverToUpdate.setGhanaCard(driverDTO.getGhanaCard());
        driverToUpdate.setResidentialAddress(driverDTO.getResidentialAddress());
        driverToUpdate.setPhoneNumber(driverDTO.getPhoneNumber());

        // If a vehicle is provided, update the assigned vehicle (assuming it's allowed)
       /* if (driverDTO.getAssignedVehicleLicensePlate() != null) {
            Optional<Vehicle> assignedVehicle = vehicleRepository.findByLicensePlate(driverDTO.getAssignedVehicleLicensePlate());
            assignedVehicle.ifPresent(driverToUpdate::setAssignedVehicle);
        }
*/
        // Save the updated driver to the repository
        Driver updatedDriver = driverRepository.save(driverToUpdate);

        // Return the updated driver as a DTO
        return convertToDTO(updatedDriver);
    }


    @Transactional
    public void deleteDriver(UUID driverId) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Driver with ID " + driverId + " not found"));
        driverRepository.delete(driver);
    }

    // Convert entity to DTO
    private DriverDTO convertToDTO(Driver driver) {
        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setId(driver.getId());
        driverDTO.setFullName(driver.getFullName());
        driverDTO.setGhanaCard(driver.getGhanaCard());
        driverDTO.setResidentialAddress(driver.getResidentialAddress());
        driverDTO.setImageUrl(driver.getImageUrl());
        driverDTO.setPhoneNumber(driver.getPhoneNumber());
        driverDTO.setAssignedVehicleId(driver.getAssignedVehicleId() != null ? driver.getAssignedVehicleId().getId() : null);
        return driverDTO;
    }

    // Convert DTO to entity
    private Driver convertToEntity(DriverDTO driverDTO, User owner, Vehicle assignedVehicle) {
        Driver driver = new Driver();
        driver.setOwner(owner);
        driver.setAssignedVehicleId(assignedVehicle);
        driver.setFullName(driverDTO.getFullName());
        driver.setGhanaCard(driverDTO.getGhanaCard());
        driver.setResidentialAddress(driverDTO.getResidentialAddress());
        driver.setImageUrl(driverDTO.getImageUrl());
        driver.setPhoneNumber(driverDTO.getPhoneNumber());
        return driver;
    }
}
