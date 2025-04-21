package com.pulse.saving.model;

import jakarta.persistence.*;
import com.pulse.user.model.Patient;
import com.pulse.pharmacy.model.Pharmacy;

@Entity
@Table(name = "SavedPharmacy")
public class SavedPharmacy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SavedPharmacyID")
    private Long savedPharmacyId;

    @ManyToOne
    @JoinColumn(name = "PatientID", referencedColumnName = "PatientID")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "PharmacyID", referencedColumnName = "PharmacyID")
    private Pharmacy pharmacy;

    // Getters and setters
    public Long getSavedPharmacyId() {
        return savedPharmacyId;
    }

    public void setSavedPharmacyId(Long savedPharmacyId) {
        this.savedPharmacyId = savedPharmacyId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Pharmacy getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }
}
