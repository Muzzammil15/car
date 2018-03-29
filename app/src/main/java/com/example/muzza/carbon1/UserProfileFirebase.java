package com.example.muzza.carbon1;

/**
 * Created by muzza on 19-Mar-18.
 */

public class UserProfileFirebase {

    String firstname;
    String lastname;
    String age;
    String email;
    String vehicle;
    String vehicleCateg;

    public UserProfileFirebase(String firstname, String lastname, String age, String email, String vehicle, String vehicleCateg) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.email = email;
        this.vehicle = vehicle;
        this.vehicleCateg = vehicleCateg;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getVehicleCateg() {
        return vehicleCateg;
    }

    public void setVehicleCateg(String vehicleCateg) {
        this.vehicleCateg = vehicleCateg;
    }
}
