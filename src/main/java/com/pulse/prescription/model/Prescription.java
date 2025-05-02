package com.pulse.prescription.model;


import com.pulse.medicalrecord.model.MedicalRecordEntry;
import com.pulse.user.model.Doctor;
import jakarta.persistence.*;


@Entity
@Table(name = "prescription")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prescriptionId;

    @Column(length = 2000)
    private String notes;

    @Column(nullable = false)
    private String status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "medical_record_entry_id", nullable = false)
    private MedicalRecordEntry medicalRecordEntry;

    // Constructors
    public Prescription() {}

    public Prescription(Long prescriptionId, String notes, String status, Doctor doctor, MedicalRecordEntry medicalRecordEntry) {
        this.prescriptionId = prescriptionId;
        this.notes = notes;
        this.status = status;
        this.doctor = doctor;
        this.medicalRecordEntry = medicalRecordEntry;
    }

    // Getters & Setters
    public Long getPrescriptionId() { return prescriptionId; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }
    public MedicalRecordEntry getMedicalRecordEntry() { return medicalRecordEntry; }
    public void setMedicalRecordEntry(MedicalRecordEntry medicalRecordEntry) { this.medicalRecordEntry = medicalRecordEntry; }
}
