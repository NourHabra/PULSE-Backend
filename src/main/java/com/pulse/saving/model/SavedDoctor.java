package com.pulse.saving.model;

import jakarta.persistence.*;
import com.pulse.user.model.Patient;
import com.pulse.user.model.Doctor;
@Entity
@Table(name = "SavedDoctor")
public class SavedDoctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SavedDoctorID")
    private Long savedDoctorId;

    @ManyToOne
    @JoinColumn(name = "PatientID", referencedColumnName = "PatientID")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "DoctorID", referencedColumnName = "DoctorID")
    private Doctor doctor;

    // Getters and setters
    public Long getSavedDoctorId() {
        return savedDoctorId;
    }

    public void setSavedDoctorId(Long savedDoctorId) {
        this.savedDoctorId = savedDoctorId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
