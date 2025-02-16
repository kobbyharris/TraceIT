package com.vehicletrackingsys.api.controllers;


import com.vehicletrackingsys.api.dtos.RealTrackDTO;
import com.vehicletrackingsys.api.dtos.TrackingDTO;
import com.vehicletrackingsys.api.models.Tracking;
import com.vehicletrackingsys.api.services.TrackingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/t")
public class TrackingController {
    private final TrackingService trackingService;

    @Autowired
    public TrackingController(TrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @GetMapping("/tracking")
    public String showTrackingPage(Model model, HttpSession session) {
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("pageLabel", "Tracking");
        return "user/tracking";
    }

    @PostMapping("/tracking/update")
    public ResponseEntity<String> updateTrackingData(@RequestBody RealTrackDTO realTrackDTO) {
        System.out.println("Received Tracking Data From Unknown Device");

        trackingService.updateTrackingUnit(realTrackDTO);

        return ResponseEntity.ok("Tracking data updated successfully");

    }

    @GetMapping("/tracking/units")
    public String showUnitsPage(Model model, HttpSession session) {
        model.addAttribute("username", session.getAttribute("username"));
        trackingService.prepareTrackingPageData(model, session);
        return "user/unit";
    }


    @GetMapping("/tracking/json")
    @ResponseBody
    public List<TrackingDTO> getAllTrackingsWithVehicles(HttpSession session) {
        return trackingService.getAllTrackings(session);
    }

    @PostMapping("/tracking/createunit")
    public String createTracking(@RequestParam String url,
                                 @RequestParam String identifier,
                                 @RequestParam String imei,
                                 @RequestParam String licensePlate,
                                 @RequestParam(required = false, defaultValue = "0.0") String longitude,
                                 @RequestParam(required = false, defaultValue = "0.0") String latitude,
                                 Model model, HttpSession session) {
        model.addAttribute("username", session.getAttribute("username"));
        double longitudeValue = 0.0;
        double latitudeValue = 0.0;

        try {
            if (longitude != null && !longitude.isEmpty()) {
                longitudeValue = Double.parseDouble(longitude);
            }
            if (latitude != null && !latitude.isEmpty()) {
                latitudeValue = Double.parseDouble(latitude);
            }
        } catch (NumberFormatException e) {
            model.addAttribute("error", "Invalid location values provided");
            return "user/unit";
        }

        if (longitudeValue == 0.0 && latitudeValue == 0.0) {
            model.addAttribute("error", "Location access is needed");
            return "user/unit";
        }

        // Create TrackingDTO and populate with data
        TrackingDTO trackingDTO = new TrackingDTO();
        trackingDTO.setId(null);
        trackingDTO.setServerUrl(url);
        trackingDTO.setIdentifier(identifier);
        trackingDTO.setGpsIMEI(imei);
        trackingDTO.setLicenseNumber(licensePlate);
        trackingDTO.setLongitude(longitudeValue);
        trackingDTO.setLatitude(latitudeValue);
        trackingDTO.setStatus("PARKED");


        return trackingService.createTracking(trackingDTO, model, session);
    }


    @GetMapping("/tracking/edit_unit/{id}")
    public String editUnitPage(@PathVariable UUID id, Model model, HttpSession session) {
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("pageLabel", "Edit unit");
        Optional<TrackingDTO> optionalTracking = trackingService.getTrackingById(id);

        if (optionalTracking.isPresent()) {
            TrackingDTO trackingData = optionalTracking.get();
            model.addAttribute("trackingData", trackingData);
            model.addAttribute("unitId", id);
            return "user/edit-unit";
        }

        model.addAttribute("error", "Tracking unit not found");
        return "user/unit";
    }

    @PostMapping("/tracking/edit_unit/{id}")
    public String editUnit(@PathVariable UUID id,
                           @RequestParam String url,
                           @RequestParam String identifier,
                           @RequestParam String imei,
                           @RequestParam String licensePlate,
                           @RequestParam(required = false, defaultValue = "0.0") String longitude,
                           @RequestParam(required = false, defaultValue = "0.0") String latitude,
                           Model model,
                           HttpSession session) {

        model.addAttribute("username", session.getAttribute("username"));
        double longitudeValue = 0.0;
        double latitudeValue = 0.0;

        try {
            if (longitude != null && !longitude.isEmpty()) {
                longitudeValue = Double.parseDouble(longitude);
            }
            if (latitude != null && !latitude.isEmpty()) {
                latitudeValue = Double.parseDouble(latitude);
            }
        } catch (NumberFormatException e) {
            model.addAttribute("error", "Invalid location values provided");
            return "user/unit";
        }

        if (longitudeValue == 0.0 && latitudeValue == 0.0) {
            model.addAttribute("error", "Location access is needed");
            return "user/unit";
        }

        TrackingDTO trackingDTO = new TrackingDTO();
        session.setAttribute("currentUnitId", id);
        trackingDTO.setServerUrl(url);
        trackingDTO.setIdentifier(identifier);
        trackingDTO.setGpsIMEI(imei);
        trackingDTO.setLicenseNumber(licensePlate);
        trackingDTO.setLongitude(longitudeValue);
        trackingDTO.setLatitude(latitudeValue);
        trackingDTO.setStatus("PARKED");
        return trackingService.updateTracking(trackingDTO, model, session);
    }

    @PostMapping("/tracking/delete_unit/{id}")
    public String deleteUnit(@PathVariable UUID id, Model model, HttpSession session) {
        TrackingDTO trackingDTO = new TrackingDTO();
        trackingDTO.setId(id);

        if (!trackingService.deleteUnit(trackingDTO)) {
            model.addAttribute("error", "Unit not found");
            return "user/units";
        }

        return "redirect:/t/tracking/units";
    }


}
