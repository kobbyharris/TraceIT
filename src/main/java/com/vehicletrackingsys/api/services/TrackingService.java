package com.vehicletrackingsys.api.services;

import com.vehicletrackingsys.api.dtos.RealTrackDTO;
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

    private void prepareTrackingPageDataById(Model model, TrackingDTO trackingDTO) {
        Optional<Tracking> optionalTracking = trackingRepository.findById(trackingDTO.getId());

        if (optionalTracking.isPresent()) {
            Tracking tracking = optionalTracking.get();

            // Convert entity back to DTO to prefill form inputs
            TrackingDTO trackingData = convertToDto(tracking);

            // Add DTO to the model so it pre-fills the form
            model.addAttribute("trackingData", trackingData);
        } else {
            // If no entity is found, still send back the original DTO with the user input
            model.addAttribute("trackingData", trackingDTO);
        }
    }


    public List<TrackingDTO> getAllTrackings(HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");
        List<Vehicle> vehicles = vehicleRepository.findByOwnerEmail(userEmail);
        List<Tracking> trackings = trackingRepository.findByVehicleIn(vehicles);

        return trackings.stream()
                .map(tracking -> {
                    // Map each tracking to a tracking DTO
                    TrackingDTO trackingDTO = new TrackingDTO();
                    trackingDTO.setId(tracking.getId());
                    trackingDTO.setTrackUnit(tracking.getVehicle().getId());
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
            return true;
        }
        return false;
    }

    @Transactional
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


    @Transactional
    public String updateTracking(TrackingDTO trackingDTO, Model model, HttpSession session) {
        UUID currentUnitId = (UUID) session.getAttribute("currentUnitId");
        Optional<Vehicle> optionalVehicle = vehicleRepository.findByLicensePlate(trackingDTO.getLicenseNumber());

        if (optionalVehicle.isEmpty()) {
            model.addAttribute("error", "No vehicle found with that license plate number");
            prepareTrackingPageDataById(model, trackingDTO);
            return "user/edit-unit";
        }

        Optional<Tracking> optionalTracking = trackingRepository.findByGpsIMEI(trackingDTO.getGpsIMEI());

        if (optionalTracking.isPresent() && !optionalTracking.get().getId().equals(currentUnitId)) {
            model.addAttribute("error", "IMEI is already in use");
            prepareTrackingPageDataById(model, trackingDTO);
            return "user/edit-unit";
        }
        trackingDTO.setId((UUID)session.getAttribute("currentUnitId"));
        Vehicle vehicle = optionalVehicle.get();
        Tracking tracking = convertToEntity(trackingDTO, vehicle);
        trackingRepository.save(tracking);

        model.addAttribute("success", "Tracking unit updated successfully");
        System.out.println("success");
        prepareTrackingPageData(model, session);
        return "user/unit";
    }

    @Transactional
    public void updateTrackingUnit(RealTrackDTO realTrackDTO) {
        String gpsIMEI = realTrackDTO.getGpsIMEI();

        // Convert status from String to Enum
        Tracking.TrackingStatus status = Tracking.TrackingStatus.valueOf(realTrackDTO.getStatus().toUpperCase());

        trackingRepository.updateTracking(
                gpsIMEI,
                realTrackDTO.getLatitude(),
                realTrackDTO.getLongitude(),
                status
        );

        System.out.println("Tracking updated for GPS Device ID: " + gpsIMEI);
    }

    public TrackingDTO convertToDto(Tracking tracking) {
        TrackingDTO dto = new TrackingDTO();
        dto.setTrackUnit(tracking.getVehicle().getId());
        dto.setLicenseNumber(tracking.getVehicle().getLicensePlate());
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
        tracking.setId(dto.getId());
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
