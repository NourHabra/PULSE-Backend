package com.pulse.labresult.model;


import com.pulse.laboratory.model.Laboratory;
import com.pulse.test.model.Test;
import com.pulse.user.model.LabTechnician;
import com.pulse.medicalrecord.model.MedicalRecordEntry;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lab_result")
public class LabResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testResultId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "laboratory_id", referencedColumnName = "laboratoryId")
    private Laboratory laboratory;

    @ManyToOne(optional = false)
    @JoinColumn(name = "test_id", referencedColumnName = "testId")
    private Test test;

    @ManyToOne(optional = false)
    @JoinColumn(name = "technician_id", referencedColumnName = "labTechnicianId")
    private LabTechnician technician;


    private String status;

    private String resultsAttachment;
    @Column(length = 2000)
    private String technicianNotes;

    @OneToOne(optional = false)
    @JoinColumn(name = "medical_record_entry_id", referencedColumnName = "medicalRecordEntryId")
    private MedicalRecordEntry medicalRecordEntry;

    public LabResult() {
    }

    public LabResult(Laboratory laboratory, Test test, LabTechnician technician,  String status, String resultsAttachment, String technicianNotes, MedicalRecordEntry medicalRecordEntry) {
        this.laboratory = laboratory;
        this.test = test;
        this.technician = technician;
        this.status = status;
        this.resultsAttachment = resultsAttachment;
        this.technicianNotes = technicianNotes;
        this.medicalRecordEntry = medicalRecordEntry;
    }

    public Long getTestResultId() {
        return testResultId;
    }

    public void setTestResultId(Long testResultId) {
        this.testResultId = testResultId;
    }

    public Laboratory getLaboratory() {
        return laboratory;
    }

    public void setLaboratory(Laboratory laboratory) {
        this.laboratory = laboratory;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public LabTechnician getTechnician() {
        return technician;
    }

    public void setTechnician(LabTechnician technician) {
        this.technician = technician;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResultsAttachment() {
        return resultsAttachment;
    }

    public void setResultsAttachment(String resultsAttachment) {
        this.resultsAttachment = resultsAttachment;
    }

    public String getTechnicianNotes() {
        return technicianNotes;
    }

    public void setTechnicianNotes(String technicianNotes) {
        this.technicianNotes = technicianNotes;
    }

    public MedicalRecordEntry getMedicalRecordEntry() {
        return medicalRecordEntry;
    }

    public void setMedicalRecordEntry(MedicalRecordEntry medicalRecordEntry) {
        this.medicalRecordEntry = medicalRecordEntry;
    }
}