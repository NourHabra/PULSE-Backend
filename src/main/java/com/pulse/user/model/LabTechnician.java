package com.pulse.user.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pulse.laboratory.model.Laboratory;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;


@Entity
@Table(name = "LabTechnician")
@PrimaryKeyJoinColumn(name = "LabTechnicianID")
public class LabTechnician extends User {

    @Column(name = "LicenseNumber")
    private String licenseNumber;

    @ManyToOne
    @JoinColumn(name = "WorkingLabID", nullable = true)
    @JsonBackReference
    private Laboratory workingLab;

    @Column(name = "TechnicianRole")
    private String technicianRole;

    public LabTechnician() {
        super.setRole("LAB_TECHNICIAN");
    }

    // Getters & Setters
    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Laboratory getWorkingLab() {
        return workingLab;
    }
    @JsonProperty("laboratoryId")                      // serialise as simple field
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Long getWorkingLabId(){return workingLab != null ? workingLab.getLaboratoryId() : null;}
    public void setWorkingLab(Laboratory workingLab) {
        this.workingLab = workingLab;
    }

    public String getTechnicianRole() {
        return technicianRole;
    }

    public void setTechnicianRole(String technicianRole) {
        this.technicianRole = technicianRole;
    }
}
