package com.pulse.saving.model;

import jakarta.persistence.*;
import com.pulse.user.model.Patient;
import com.pulse.user.model.Doctor;

@Entity
@Table(name = "saved_doctor")
public class SavedDoctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "saved_doctorid")
    private Long savedDoctorId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "patientid", referencedColumnName = "patientid")
    private Patient patient;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "doctorid", referencedColumnName = "doctorid")
    private Doctor doctor;

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
