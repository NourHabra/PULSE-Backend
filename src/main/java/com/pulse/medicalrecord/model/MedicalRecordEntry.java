package com.pulse.medicalrecord.model;


import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.pulse.user.model.Patient;
@Entity
@Table(name = "medical_record_entry")
public class MedicalRecordEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long medicalRecordEntryId;

    private String title;

    private LocalDateTime timestamp;

    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;



    public MedicalRecordEntry() {
    }

    public MedicalRecordEntry(Long medicalRecordEntryId, String title, LocalDateTime timestamp, Patient patient) {
        this.medicalRecordEntryId = medicalRecordEntryId;
        this.title = title;
        this.timestamp = timestamp;
        this.patient = patient;
    }

    public Long getMedicalRecordEntryId() {
        return medicalRecordEntryId;
    }

    public void setMedicalRecordEntryId(Long medicalRecordEntryId) {
        this.medicalRecordEntryId = medicalRecordEntryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}