package com.pulse.drug.dto;


public class PrescriptionDrugRequest {
    private Long prescriptionId;
    private Long drugId;
    private String duration;
    private String dosage;
    private String notes;

    public PrescriptionDrugRequest() {}

    public PrescriptionDrugRequest(Long prescriptionId, Long drugId, String duration, String dosage, String notes) {
        this.prescriptionId = prescriptionId;
        this.drugId = drugId;
        this.duration = duration;
        this.dosage = dosage;
        this.notes = notes;
    }

    public Long getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Long prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public Long getDrugId() {
        return drugId;
    }

    public void setDrugId(Long drugId) {
        this.drugId = drugId;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
