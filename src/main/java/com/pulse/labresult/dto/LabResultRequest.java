package com.pulse.labresult.dto;


public class LabResultRequest {
    private Long labId;
    private Long testId;
    private String status;
    private String resultsAttachment;
    private String technicianNotes;
    private String mreTitle;
    private Long patientId;

    public LabResultRequest() {
    }

    public LabResultRequest(Long labId, Long testId, String status, String resultsAttachment, String technicianNotes, String mreTitle, Long patientId) {
        this.labId = labId;
        this.testId = testId;
        this.status = status;
        this.resultsAttachment = resultsAttachment;
        this.technicianNotes = technicianNotes;
        this.mreTitle = mreTitle;
        this.patientId = patientId;
    }

    public Long getLabId() {
        return labId;
    }

    public void setLabId(Long labId) {
        this.labId = labId;
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
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