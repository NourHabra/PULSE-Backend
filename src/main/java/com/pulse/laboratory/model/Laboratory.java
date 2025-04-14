package com.pulse.laboratory.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pulse.user.model.LabTechnician;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Laboratory")
public class Laboratory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LaboratoryID")
    private Long laboratoryId;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "LicenseNumber", nullable = false, unique = true)
    private String licenseNumber;

    @Column(name = "WorkingHours")
    private String workingHours;

    @OneToOne
    @JoinColumn(name = "ManagerID")
    @JsonManagedReference
    private LabTechnician manager;

    @Column(name = "Phone")
    private String phone;

    @Column(name = "Address")
    private String address;

    @Column(name = "LocationCoordinates")
    private String locationCoordinates;

    @OneToMany(mappedBy = "workingLab", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<LabTechnician> technicians;



    // === Constructors ===
    public Laboratory() {}

    // === Getters & Setters ===
    public Long getLaboratoryId() {
        return laboratoryId;
    }

    public void setLaboratoryId(Long laboratoryId) {
        this.laboratoryId = laboratoryId;
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

    public LabTechnician getManager() {
        return manager;
    }

    public void setManager(LabTechnician manager) {
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


}
