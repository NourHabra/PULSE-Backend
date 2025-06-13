package com.pulse.emergencyevent.model;


import com.pulse.medicalrecord.model.MedicalRecordEntry;
import com.pulse.user.model.Doctor;
import com.pulse.user.model.EmergencyWorker;
import jakarta.persistence.*;

@Entity
@Table(name = "emergency_event")
public class EmergencyEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emergencyEventId;

    @Column(length = 2000)
    private String notes;

    @ManyToOne(optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "medical_record_entry_id", nullable = false)
    private MedicalRecordEntry medicalRecordEntry;

    // Default constructor
    public EmergencyEvent() {
    }

    public EmergencyEvent(Long emergencyEventId, String notes, Doctor doctor, MedicalRecordEntry medicalRecordEntry) {
        this.emergencyEventId = emergencyEventId;
        this.notes = notes;
        this.doctor = doctor;
        this.medicalRecordEntry = medicalRecordEntry;
    }

    // Getters and setters
    public Long getEmergencyEventId() {
        return emergencyEventId;
    }

    public void setEmergencyEventId(Long emergencyEventId) {
        this.emergencyEventId = emergencyEventId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public MedicalRecordEntry getMedicalRecordEntry() {
        return medicalRecordEntry;
    }

    public void setMedicalRecordEntry(MedicalRecordEntry medicalRecordEntry) {
        this.medicalRecordEntry = medicalRecordEntry;
    }

}