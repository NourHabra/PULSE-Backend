package com.pulse.emergencyevent.model;


import com.pulse.medicalrecord.model.MedicalRecordEntry;
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
    @JoinColumn(name = "emergency_worker_id", nullable = false)
    private EmergencyWorker emergencyWorker;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "medical_record_entry_id", nullable = false)
    private MedicalRecordEntry medicalRecordEntry;

    // Default constructor
    public EmergencyEvent() {
    }

    // All-args constructor
    public EmergencyEvent(Long emergencyEventId, String notes, EmergencyWorker emergencyWorker, MedicalRecordEntry medicalRecordEntry) {
        this.emergencyEventId = emergencyEventId;
        this.notes = notes;
        this.emergencyWorker = emergencyWorker;
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

    public EmergencyWorker getEmergencyWorker() {
        return emergencyWorker;
    }

    public void setEmergencyWorker(EmergencyWorker emergencyWorker) {
        this.emergencyWorker = emergencyWorker;
    }

    public MedicalRecordEntry getMedicalRecordEntry() {
        return medicalRecordEntry;
    }

    public void setMedicalRecordEntry(MedicalRecordEntry medicalRecordEntry) {
        this.medicalRecordEntry = medicalRecordEntry;
    }

}