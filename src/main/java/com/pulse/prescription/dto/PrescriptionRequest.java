package com.pulse.prescription.dto;


public class PrescriptionRequest {
    private String notes;
    private String status;
    private String mreTitle;
    private Long patientId;

    public PrescriptionRequest() {}
    public PrescriptionRequest(String notes, String status, String mreTitle, Long patientId) {
        this.notes = notes;
        this.status = status;
        this.mreTitle = mreTitle;
        this.patientId = patientId;
    }
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMreTitle() {
        return mreTitle;
    }

    public void setMreTitle(String mreTitle) {
        this.mreTitle = mreTitle;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
}
