package com.pulse.saving.model;

import jakarta.persistence.*;
import com.pulse.user.model.Patient;
import com.pulse.laboratory.model.Laboratory;

@Entity
@Table(name = "SavedLaboratory")
public class SavedLaboratory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SavedLaboratoryID")
    private Long savedLaboratoryId;

    @ManyToOne
    @JoinColumn(name = "PatientID", referencedColumnName = "PatientID")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "LaboratoryID", referencedColumnName = "LaboratoryID")
    private Laboratory laboratory;

    // Getters and setters
    public Long getSavedLaboratoryId() {
        return savedLaboratoryId;
    }

    public void setSavedLaboratoryId(Long savedLaboratoryId) {
        this.savedLaboratoryId = savedLaboratoryId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Laboratory getLaboratory() {
        return laboratory;
    }

    public void setLaboratory(Laboratory laboratory) {
        this.laboratory = laboratory;
    }
}
