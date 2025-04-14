package com.pulse.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "EmergencyWorker")
@PrimaryKeyJoinColumn(name = "EmergencyWorkerID") // PK = FK to User(UserID)
public class EmergencyWorker extends User {

    @Column(name = "LicenseNumber")
    private String licenseNumber;

    public EmergencyWorker() {
        super.setRole("emergency_worker"); // or any preferred role label
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }
}
