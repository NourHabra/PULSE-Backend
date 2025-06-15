package com.pulse.user.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pulse.pharmacy.model.Pharmacy;
import jakarta.persistence.*;

@Entity
@Table(name = "Pharmacist")
@PrimaryKeyJoinColumn(name = "PharmacistID")
public class Pharmacist extends User {

    @Column(name = "LicenseNumber")
    private String licenseNumber;

    @ManyToOne
    @JoinColumn(name = "WorkingPharmacyID")
    @JsonBackReference
    private Pharmacy workingPharmacy;


    @Column(name = "PharmacistRole")
    private String pharmacistRole;

    public Pharmacist() {
        super.setRole("pharmacist");
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Pharmacy getWorkingPharmacy() {
        return workingPharmacy;
    }

    public void setWorkingPharmacy(Pharmacy workingPharmacy) {
        this.workingPharmacy = workingPharmacy;
    }

    public String getPharmacistRole() {
        return pharmacistRole;
    }

    public void setPharmacistRole(String pharmacistRole) {
        this.pharmacistRole = pharmacistRole;
    }
}
