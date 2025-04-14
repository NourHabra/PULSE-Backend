package com.pulse.user.dto;

public class PharmacistRegisterDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    // Pharmacist-specific fields
    private String licenseNumber;
    private Long pharmacyId; // Reference to the Pharmacy entity
    private String pharmacistRole; // "MANAGER" or "PHARMACIST"

    public PharmacistRegisterDto() {}

    // --- Getters & Setters ---

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }
    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Long getPharmacyId() {
        return pharmacyId;
    }
    public void setPharmacyId(Long pharmacyId) {
        this.pharmacyId = pharmacyId;
    }

    public String getPharmacistRole() {
        return pharmacistRole;
    }
    public void setPharmacistRole(String pharmacistRole) {
        this.pharmacistRole = pharmacistRole;
    }
}
