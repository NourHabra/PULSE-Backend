package com.pulse.medicalrecord.dto;

import com.pulse.medicalrecord.model.MedicalRecordEntry;
import com.pulse.diagnosis.model.Diagnosis;
import com.pulse.emergencyevent.model.EmergencyEvent;
import com.pulse.prescription.model.Prescription;
import com.pulse.labresult.model.LabResult;
import java.time.LocalDateTime;

public class MedicalRecordEntryDTO {
    private Long entryId;
    private String title;
    private LocalDateTime timestamp;
    private Long patientId;

    private Diagnosis       diagnosis;
    private EmergencyEvent  emergencyEvent;
    private Prescription    prescription;
    private LabResult       labResult;

    public MedicalRecordEntryDTO(MedicalRecordEntry mre) {
        this.entryId   = mre.getMedicalRecordEntryId();
        this.title     = mre.getTitle();
        this.timestamp = mre.getTimestamp();
        this.patientId = mre.getPatient().getUserId();
    }


    public Long getEntryId() {
        return entryId;
    }

    public void setEntryId(Long entryId) {
        this.entryId = entryId;
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

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Diagnosis getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(Diagnosis diagnosis) {
        this.diagnosis = diagnosis;
    }

    public EmergencyEvent getEmergencyEvent() {
        return emergencyEvent;
    }

    public void setEmergencyEvent(EmergencyEvent emergencyEvent) {
        this.emergencyEvent = emergencyEvent;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public LabResult getLabResult() {
        return labResult;
    }

    public void setLabResult(LabResult labResult) {
        this.labResult = labResult;
    }

}
