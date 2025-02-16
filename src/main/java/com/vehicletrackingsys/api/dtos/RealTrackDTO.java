package com.vehicletrackingsys.api.dtos;

public class RealTrackDTO {
    private String gpsIMEI;
    private String identifier;
    private double latitude;
    private double longitude;
    private String status;
    private String timestamp;



    public String getGpsIMEI() {
        return gpsIMEI;
    }

    public void setGpsIMEI(String gpsIMEI) {
        this.gpsIMEI = gpsIMEI;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
