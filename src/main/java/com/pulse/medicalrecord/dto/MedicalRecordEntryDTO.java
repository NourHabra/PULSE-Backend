package com.pulse.medicalrecord.dto;

import com.pulse.diagnosis.model.Diagnosis;
import com.pulse.emergencyevent.model.EmergencyEvent;
import com.pulse.labresult.model.LabResult;
import com.pulse.drug.model.PrescriptionDrug;
import java.time.LocalDateTime;
import java.util.List;

public class MedicalRecordEntryDTO {
    private Long entryId;
    private String title;
    private LocalDateTime timestamp;
    private Long patientId;

    private Diagnosis       diagnosis;
    private EmergencyEvent  emergencyEvent;

    private List<PrescriptionDrug> prescriptionDrugs;

    public MedicalRecordEntryDTO(com.pulse.medicalrecord.model.MedicalRecordEntry mre) {
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

    public List<PrescriptionDrug> getPrescriptionDrugs() {
        return prescriptionDrugs;
    }
    public void setPrescriptionDrugs(List<PrescriptionDrug> prescriptionDrugs) {
        this.prescriptionDrugs = prescriptionDrugs;
    }
}
