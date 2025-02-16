package com.vehicletrackingsys.api.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tracking")
public class Tracking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "track_unit", referencedColumnName = "id", nullable = false)
    private Vehicle vehicle;

    @Column(unique = true, nullable = false)
    private String gpsIMEI;
    private String serverUrl;
    private String identifier;
    private double latitude;
    private double longitude;

    @Enumerated(EnumType.STRING)
    private TrackingStatus status;

    private LocalDateTime timestamp = LocalDateTime.now();

    public enum TrackingStatus {
        MOVING,
        IDLE,
        PARKED
    }

    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getGpsIMEI() {
        return gpsIMEI;
    }

    public void setGpsIMEI(String gpsIMEI) {
        this.gpsIMEI = gpsIMEI;
    }

    public String getServerUrl() { return serverUrl; }
    public void setServerUrl(String serverUrl) { this.serverUrl = serverUrl; }

    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public TrackingStatus getStatus() { return status; }
    public void setStatus(TrackingStatus status) { this.status = status; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
