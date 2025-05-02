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
    private String officialDiagnosis;

    @Column(length = 5000)
    private String description;

    @Column(length = 2000)
    private String followUps;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medical_record_entry_id", nullable = false)
    private MedicalRecordEntry medicalRecordEntry;

    @ManyToOne(optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    public Diagnosis() {
    }

    public Diagnosis(Long diagnosisId,  String officialDiagnosis, String description, String followUps, MedicalRecordEntry medicalRecordEntry, Doctor doctor) {
        this.diagnosisId = diagnosisId;

        this.officialDiagnosis = officialDiagnosis;
        this.description = description;
        this.followUps = followUps;
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
        return officialDiagnosis;
    }

    public void setOfficialDiagnosis(String officialDiagnosis) {
        this.officialDiagnosis = officialDiagnosis;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFollowUps() {
        return followUps;
    }

    public void setFollowUps(String followUps) {
        this.followUps = followUps;
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