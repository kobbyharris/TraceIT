package com.vehicletrackingsys.api.services;

import com.vehicletrackingsys.api.dtos.TrackingDTO;
import com.vehicletrackingsys.api.models.Tracking;
import com.vehicletrackingsys.api.models.Vehicle;
import com.vehicletrackingsys.api.repositories.TrackingRepository;
import com.vehicletrackingsys.api.repositories.VehicleRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TrackingService {
    private final VehicleRepository vehicleRepository;
    private final TrackingRepository trackingRepository;

    @Autowired
    public TrackingService(VehicleRepository vehicleRepository, TrackingRepository trackingRepository) {
        this.vehicleRepository = vehicleRepository;
        this.trackingRepository = trackingRepository;
    }

    public void prepareTrackingPageData(Model model, HttpSession session) {
        List<TrackingDTO> trackingData = this.getAllTrackings(session);
        model.addAttribute("trackingData", trackingData);
        model.addAttribute("pageLabel", "Units");
    }

    public List<TrackingDTO> getAllTrackings(HttpSession session) {
        // Get the user's email from the session
        String userEmail = (String) session.getAttribute("userEmail");

        // Fetch the vehicles belonging to the logged-in user
        List<Vehicle> vehicles = vehicleRepository.findByOwnerEmail(userEmail);

        // Get tracking data based on the licensePlate of the vehicles owned by the user
        List<Tracking> trackings = trackingRepository.findByVehicleIn(vehicles);

        // Convert the tracking entities to DTOs
        return trackings.stream()
                .map(tracking -> {
                    // Map each tracking to a tracking DTO
                    TrackingDTO trackingDTO = new TrackingDTO();
                    trackingDTO.setId(tracking.getId());
                    trackingDTO.setTrackUnit(tracking.getVehicle().getLicensePlate());
                    trackingDTO.setGpsIMEI(tracking.getGpsIMEI());
                    trackingDTO.setServerUrl(tracking.getServerUrl());
                    trackingDTO.setIdentifier(tracking.getIdentifier());
                    trackingDTO.setLatitude(tracking.getLatitude());
                    trackingDTO.setLongitude(tracking.getLongitude());
                    trackingDTO.setStatus(tracking.getStatus().name());
                    trackingDTO.setTimestamp(tracking.getTimestamp());
                    return trackingDTO;
                })
                .collect(Collectors.toList());
    }

    public Optional<TrackingDTO> getTrackingById(UUID id) {
        return trackingRepository.findById(id)
                .map(this::convertToDto);
    }

    @Transactional
    public boolean deleteUnit(TrackingDTO trackingDTO) {
        if (trackingRepository.existsById(trackingDTO.getId())) {
            trackingRepository.deleteById(trackingDTO.getId());
            return true; // Deletion successful
        }
        return false; // Unit not found
    }


    public String createTracking(TrackingDTO trackingDTO, Model model, HttpSession session) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findByLicensePlate(trackingDTO.getLicenseNumber());

        if (optionalVehicle.isEmpty()) {
            model.addAttribute("error", "No vehicle found with that license plate number");
            prepareTrackingPageData(model, session);
            return "user/unit";
        }

        Optional<Tracking> optionalTracking = trackingRepository.findByGpsIMEI(trackingDTO.getGpsIMEI());
        if (optionalTracking.isPresent()) {
            model.addAttribute("error", "IMEI is already in use");
            prepareTrackingPageData(model, session);
            return "user/unit";
        }

        Vehicle vehicle = optionalVehicle.get();
        Tracking tracking = convertToEntity(trackingDTO, vehicle);
        trackingRepository.save(tracking);
        return "redirect:/t/tracking";
    }


    public TrackingDTO convertToDto(Tracking tracking) {
        TrackingDTO dto = new TrackingDTO();
        dto.setTrackUnit(tracking.getVehicle().getLicensePlate());
        dto.setGpsIMEI(tracking.getGpsIMEI());
        dto.setServerUrl(tracking.getServerUrl());
        dto.setIdentifier(tracking.getIdentifier());
        dto.setLatitude(tracking.getLatitude());
        dto.setLongitude(tracking.getLongitude());
        dto.setStatus(tracking.getStatus().name());
        dto.setTimestamp(tracking.getTimestamp());
        return dto;
    }


    public Tracking convertToEntity(TrackingDTO dto, Vehicle vehicle) {
        Tracking tracking = new Tracking();
        tracking.setVehicle(vehicle);
        tracking.setGpsIMEI(dto.getGpsIMEI());
        tracking.setIdentifier(dto.getIdentifier());
        tracking.setServerUrl(dto.getServerUrl());
        tracking.setLatitude(dto.getLatitude());
        tracking.setLongitude(dto.getLongitude());
        tracking.setStatus(Tracking.TrackingStatus.valueOf(dto.getStatus()));
        tracking.setTimestamp(LocalDateTime.now());
        return tracking;
    }
}
