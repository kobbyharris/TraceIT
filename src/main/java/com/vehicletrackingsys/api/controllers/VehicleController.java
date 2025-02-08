package com.vehicletrackingsys.api.controllers;

import com.vehicletrackingsys.api.dtos.DriverDTO;
import com.vehicletrackingsys.api.dtos.TrackingDTO;
import com.vehicletrackingsys.api.dtos.VehicleDTO;
import com.vehicletrackingsys.api.services.FileService;
import com.vehicletrackingsys.api.services.VehicleService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/t")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private FileService fileService;

    // Display all vehicles for the user
    @GetMapping("/vehicles")
    public String getVehicles(Model model, HttpSession session) {
        model.addAttribute("username", session.getAttribute("username"));
        String email = (String) session.getAttribute("userEmail");

        // Check if the user has vehicles
        if (email == null || !vehicleService.userHasVehiclesByEmail(email)) {
            model.addAttribute("pageLabel", "Vehicles");
            return "user/Vehicle/no-vehicle"; // Redirect or display message if no vehicles found
        }

        // Fetch vehicles associated with the logged-in user
        List<VehicleDTO> vehicles = vehicleService.getVehiclesByEmail(email);
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("pageLabel", "Vehicles");
        return "user/Vehicle/vehicle"; // Return to vehicle listing page
    }

    @GetMapping("/v/create")
    public String showCreateVehiclePage(Model model, HttpSession session) {
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("pageLabel", "Create vehicle");
        return "user/Vehicle/create-vehicle";
    }

    @GetMapping("/vehicle-details/{id}")
    public String getVehicleDetails(@PathVariable UUID id, Model model, HttpSession session) {
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("pageLabel", "Vehicle Details");
        UUID ownerId = (UUID) session.getAttribute("userId");
        VehicleDTO vehicleDTO = vehicleService.getVehicleById(id);
        model.addAttribute("vehicle", vehicleDTO);
        return "user/Vehicle/vehicle-details";
    }

    // Create a new vehicle
    @PostMapping("/createVehicle")
    public String createVehicle(@RequestParam String vehicleModel,
                                @RequestParam String make,
                                @RequestParam String vehicleType,
                                @RequestParam String licensePlate,
                                @RequestParam("file_input") MultipartFile file,
                                VehicleDTO vehicleDto,
                                HttpSession session, Model model) {

        model.addAttribute("username", session.getAttribute("username"));
        if (fileService.isFileTooLarge(file)) {
            model.addAttribute("error", "File size exceeds 5MB limit.");
            return "user/Vehicle/create-vehicle";
        }

        try {
            String filePath = fileService.storeFile(file);
            vehicleDto.setImageUrl(filePath);

        } catch (IOException e) {
            model.addAttribute("error", "Failed to upload file. Please try again.");
            return "user/Vehicle/create-vehicle";
        }

        vehicleDto.setModel(vehicleModel);
        vehicleDto.setMake(make);
        vehicleDto.setVehicleType(vehicleType);
        vehicleDto.setLicensePlate(licensePlate);

        if(!vehicleService.createVehicle(vehicleDto,session)) {
            model.addAttribute("error", "Vehicle with plate number already exits");
            return "user/Vehicle/create-vehicle";
        }
        return "redirect:/t/vehicles";
    }

    // Update an existing vehicle
    @PostMapping("/edit-vehicle/{id}")
    public String updateVehicle(@PathVariable UUID id, @RequestBody VehicleDTO vehicleDto) {
        vehicleDto.setId(id);
        vehicleService.updateVehicle(vehicleDto);
        return "redirect:/t/vehicles"; // Redirect after update
    }

    // Delete a vehicle by ID
    @PostMapping("/vehicles/d/{id}")
    public String deleteVehicle(@PathVariable UUID id) {
        vehicleService.deleteVehicle(id);
        return "redirect:/t/vehicles"; // Redirect after deletion
    }
}
