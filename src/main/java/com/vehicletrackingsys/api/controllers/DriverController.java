package com.vehicletrackingsys.api.controllers;

import com.vehicletrackingsys.api.dtos.DriverDTO;
import com.vehicletrackingsys.api.dtos.VehicleDTO;
import com.vehicletrackingsys.api.services.DriverService;
import com.vehicletrackingsys.api.services.FileService;
import com.vehicletrackingsys.api.services.VehicleService;
import com.vehicletrackingsys.api.utilis.EncryptionUtil;
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
public class DriverController {

    @Autowired
    private DriverService driverService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private FileService fileService;

    @Autowired
    private EncryptionUtil encryptionUtil;

    // Display all drivers for the user
    @GetMapping("/drivers")
    public String getDriversByEmail(Model model, HttpSession session) {
        UUID id = (UUID) session.getAttribute("userId");
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("pageLabel", "Drivers");

        // Check if the user has drivers
        if (id == null || !driverService.userHasDriversById(id))
            return "user/Driver/no-driver";

        // Fetch drivers associated with the logged-in user
        List<DriverDTO> drivers = driverService.getDriversByOwnerId(id);
        model.addAttribute("drivers", drivers);
        return "user/Driver/drivers"; // Return to driver listing page
    }



    @GetMapping("/driver-details/{id}")
    public String getDriverDetails(@PathVariable UUID id, Model model, HttpSession session) {
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("pageLabel", "Driver Details");
        UUID ownerId = (UUID) session.getAttribute("userId");
        DriverDTO driverDTO = driverService.getDriverById(id);
        model.addAttribute("driver", driverDTO);
        return "user/Driver/driver-details";
    }

    @GetMapping("/d/create")
    public String showCreateDriverPage(Model model, HttpSession session) {
        model.addAttribute("username", session.getAttribute("username"));
        String email = (String) session.getAttribute("userEmail");
        List<VehicleDTO> vehicles = vehicleService.getUnassignedVehiclesByEmail(email);
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("pageLabel", "Create driver");
        return "user/Driver/create-driver";
    }

    // Create a new driver
    @PostMapping("/createDriver")
    public String createDriver(@RequestParam String fullName,
                               @RequestParam String phoneNumber,
                               @RequestParam String ghanaCard,
                               @RequestParam String residentialAddress,
                               @RequestParam("file_input") MultipartFile file,
                               @RequestParam String licensePlate,
                               DriverDTO driverDto, HttpSession session, Model model) throws Exception {
        model.addAttribute("username", session.getAttribute("username"));
        DriverDTO driverDTO = new DriverDTO();

        UUID assignedVehicleId =
                !licensePlate.isEmpty()?
                        vehicleService.getVehicleIdByLicensePlateNumber(licensePlate).get().getId():null;

        if (fileService.isFileTooLarge(file)) {
            model.addAttribute("error", "File size exceeds 5MB limit.");
            return "user/Vehicle/create-vehicle";
        }

        try {
            String filePath = fileService.storeFile(file);
            driverDTO.setImageUrl(filePath);

        } catch (IOException e) {
            model.addAttribute("error", "Failed to upload file. Please try again.");
            return "user/Vehicle/create-vehicle";
        }

        driverDTO.setOwnerId((UUID)session.getAttribute("userId"));
        driverDTO.setFullName(fullName);
        driverDTO.setGhanaCard(encryptionUtil.encrypt(ghanaCard));
        driverDTO.setPhoneNumber(phoneNumber);
        driverDTO.setResidentialAddress(residentialAddress);
        driverDTO.setAssignedVehicleId(assignedVehicleId);
        driverService.createDriver(driverDTO, session);
        return "redirect:/t/drivers";
    }

    // Update an existing driver
    @PostMapping("/drivers/{id}")
    public String updateDriver(@PathVariable UUID id, @RequestBody DriverDTO driverDto, Model model) {
        // Set the driver ID in the DTO
        driverDto.setId(id);

        // Call the service to update the driver
        try {
            driverService.updateDriver(driverDto);
            model.addAttribute("successMessage", "Driver updated successfully!");
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        // Redirect to the drivers listing page with the success/error message
        return "redirect:/t/drivers";
    }


    // Delete a driver by ID
    @PostMapping("/delete-driver/{id}")
    public String deleteDriver(@PathVariable UUID id) {
        driverService.deleteDriver(id);
        return "redirect:/t/drivers";
    }
}
