package com.pulse.user.dto;

import com.pulse.laboratory.model.Laboratory;
public class LabTechnicianRegisterDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private String licenseNumber;
    private Long laboratoryId; // FK reference to Laboratory
    private String technicianRole;

    public LabTechnicianRegisterDto() {}

    // Getters & Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public Long getLaboratoryId() { return laboratoryId; }
    public void setLaboratoryId(Long laboratoryId) { this.laboratoryId = laboratoryId; }

    public String getTechnicianRole() { return technicianRole; }
    public void setTechnicianRole(String technicianRole) { this.technicianRole = technicianRole; }
}
