package com.example.muzza.carbon1;

/**
 * Created by muzza on 20-Mar-18.
 */

public class UserTrackData {

    String email;
    String currentDate;
    String totalDistance;
    String totalTime;
    String avgSpeed;
    String carbonFootprint;
    String vahicle;
    String vehicleCateg;


    public UserTrackData(String email, String currentDate, String totalDistance, String totalTime, String avgSpeed, String carbonFootprint, String vahicle, String vehicleCateg) {
        this.email = email;
        this.currentDate = currentDate;
        this.totalDistance = totalDistance;
        this.totalTime = totalTime;
        this.avgSpeed = avgSpeed;
        this.carbonFootprint = carbonFootprint;
        this.vahicle = vahicle;
        this.vehicleCateg = vehicleCateg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(String avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public String getCarbonFootprint() {
        return carbonFootprint;
    }

    public void setCarbonFootprint(String carbonFootprint) {
        this.carbonFootprint = carbonFootprint;
    }

    public String getVahicle() {
        return vahicle;
    }

    public void setVahicle(String vahicle) {
        this.vahicle = vahicle;
    }

    public String getVehicleCateg() {
        return vehicleCateg;
    }

    public void setVehicleCateg(String vehicleCateg) {
        this.vehicleCateg = vehicleCateg;
    }
}
