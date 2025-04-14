package com.pulse.pharmacy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pulse.user.model.Pharmacist;
import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
@Entity
@Table(name = "Pharmacy")
public class Pharmacy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PharmacyID")
    private Long pharmacyId;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "LicenseNumber", nullable = false, unique = true)
    private String licenseNumber;

    @Column(name = "WorkingHours")
    private String workingHours;

    @OneToOne
    @JoinColumn(name = "ManagerID")
    @JsonManagedReference
    private Pharmacist manager;


    @Column(name = "Phone")
    private String phone;

    @Column(name = "Address")
    private String address;

    @Column(name = "LocationCoordinates")
    private String locationCoordinates;

    @OneToMany(mappedBy = "workingPharmacy", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Pharmacist> pharmacists;

    // --- Getters and Setters ---

    public Long getPharmacyId() {
        return pharmacyId;
    }

    public void setPharmacyId(Long pharmacyId) {
        this.pharmacyId = pharmacyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Pharmacist getManager() {
        return manager;
    }

    public void setManager(Pharmacist manager) {
        this.manager = manager;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocationCoordinates() {
        return locationCoordinates;
    }

    public void setLocationCoordinates(String locationCoordinates) {
        this.locationCoordinates = locationCoordinates;
    }

    public List<Pharmacist> getPharmacists() {
        return pharmacists;
    }

    public void setPharmacists(List<Pharmacist> pharmacists) {
        this.pharmacists = pharmacists;
    }
}
