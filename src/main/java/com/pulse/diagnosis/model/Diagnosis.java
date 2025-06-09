package com.pulse.diagnosis.model;


import com.pulse.medicalrecord.model.MedicalRecordEntry;
import com.pulse.user.model.Doctor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "diagnosis")
public class Diagnosis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diagnosisId;


    @Column(length = 2000)
    private String attachment;

    @Column(length = 5000)
    private String description;

    @Column(length = 2000)
    private String ICD10;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medical_record_entry_id", nullable = false)
    private MedicalRecordEntry medicalRecordEntry;

    @ManyToOne(optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    public Diagnosis() {
    }

    public Diagnosis(Long diagnosisId,  String attachment, String description, String ICD10, MedicalRecordEntry medicalRecordEntry, Doctor doctor) {
        this.diagnosisId = diagnosisId;

        this.attachment = attachment;
        this.description = description;
        this.ICD10 = ICD10;
        this.medicalRecordEntry = medicalRecordEntry;
        this.doctor = doctor;
    }

    public Long getDiagnosisId() {
        return diagnosisId;
    }

    public void setDiagnosisId(Long diagnosisId) {
        this.diagnosisId = diagnosisId;
    }

    public String getOfficialDiagnosis() {
        return attachment;
    }

    public void setOfficialDiagnosis(String attachment) {
        this.attachment = attachment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFollowUps() {
        return ICD10;
    }

    public void setFollowUps(String ICD10) {
        this.ICD10 = ICD10;
    }

    public MedicalRecordEntry getMedicalRecordEntry() {
        return medicalRecordEntry;
    }

    public void setMedicalRecordEntry(MedicalRecordEntry medicalRecordEntry) {
        this.medicalRecordEntry = medicalRecordEntry;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}