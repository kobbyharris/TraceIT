package com.vehicletrackingsys.api.dtos;

import com.vehicletrackingsys.api.models.Tracking;
import com.vehicletrackingsys.api.models.Vehicle;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

public class TrackingDTO {
    private UUID id;
    private UUID trackUnit;
    private String licenseNumber;
    private String gpsIMEI;
    private String serverUrl;
    private String identifier;
    private double latitude;
    private double longitude;
    private String status;
    private LocalDateTime timestamp;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTrackUnit() {
        return trackUnit;
    }

    public void setTrackUnit(UUID trackUnit) {
        this.trackUnit = trackUnit;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getGpsIMEI() {
        return gpsIMEI;
    }

    public void setGpsIMEI(String gpsIMEI) {
        this.gpsIMEI = gpsIMEI;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
