package com.vehicletrackingsys.api.dtos;

import jakarta.persistence.Column;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Data
public class DriverDTO {
    private UUID id;
    private UUID ownerId;
    private String fullName;
    private String ghanaCard;
    private String residentialAddress;
    private String phoneNumber;
    private String imageUrl;
    private UUID assignedVehicleId;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGhanaCard() {
        return ghanaCard;
    }

    public void setGhanaCard(String ghanaCard) {
        this.ghanaCard = ghanaCard;
    }

    public String getResidentialAddress() {
        return residentialAddress;
    }

    public void setResidentialAddress(String residentialAddress) {
        this.residentialAddress = residentialAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public UUID getAssignedVehicleId() {
        return assignedVehicleId;
    }

    public void setAssignedVehicleId(UUID assignedVehicleId) {
        this.assignedVehicleId = assignedVehicleId;
    }
}
