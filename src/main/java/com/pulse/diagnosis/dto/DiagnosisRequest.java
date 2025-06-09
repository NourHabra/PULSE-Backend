package com.pulse.diagnosis.dto;


public class DiagnosisRequest {
    private String attachment;
    private String description;
    private String ICD10;
    private String mreTitle;
    private Long patientId;

    public DiagnosisRequest() {}
    public DiagnosisRequest(String attachment, String description, String ICD10, String mreTitle, Long patientId) {
        this.attachment = attachment;
        this.description = description;
        this.ICD10 = ICD10;
        this.mreTitle = mreTitle;
        this.patientId = patientId;
    }
    public String getOfficialDiagnosis() { return attachment; }
    public void setOfficialDiagnosis(String attachment) { this.attachment = attachment; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getFollowUps() { return ICD10; }
    public void setFollowUps(String ICD10) { this.ICD10 = ICD10; }
    public String getMreTitle() { return mreTitle; }
    public void setMreTitle(String mreTitle) { this.mreTitle = mreTitle; }
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
}