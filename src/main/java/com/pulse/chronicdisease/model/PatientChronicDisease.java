package com.pulse.chronicdisease.model;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.io.Serializable;
import com.pulse.user.model.Patient;


@Entity
@Table(name = "patient_chronic_disease")
public class PatientChronicDisease {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chronic_disease_id", nullable = false)
    private ChronicDisease chronicDisease;

    private String intensity;
    private LocalDate startDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public ChronicDisease getChronicDisease() {
        return chronicDisease;
    }

    public void setChronicDisease(ChronicDisease chronicDisease) {
        this.chronicDisease = chronicDisease;
    }

    public String getIntensity() {
        return intensity;
    }

    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

}