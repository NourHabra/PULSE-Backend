package com.pulse.user.model;


import jakarta.persistence.*;



@Entity
@PrimaryKeyJoinColumn(name = "DoctorID")
public class Doctor extends User {

    @Column(name = "Specialization")
    private String specialization;

    @Column(name = "LicenseNumber")
    private String licenseNumber;

    @Column(name = "WorkingHours")
    private String workingHours;

    @Column(name = "Biography")
    private String biography;
    @Column(name = "Coordinates")
    private String coordinates;
    public Doctor() {
        // Optional: Set role = "doctor" by default
        super.setRole("doctor");
    }

    // Additional constructors, getters, setters
    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }
}
