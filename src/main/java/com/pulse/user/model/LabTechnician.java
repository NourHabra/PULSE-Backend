package com.pulse.user.model;

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
    @JoinColumn(name = "WorkingLabID")
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
    public Long getWorkingLabId(){return workingLab.getLaboratoryId();}
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
