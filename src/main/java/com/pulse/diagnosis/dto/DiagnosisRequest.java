package com.pulse.diagnosis.dto;


public class DiagnosisRequest {
    private String officialDiagnosis;
    private String description;
    private String followUps;
    private String mreTitle;
    private Long patientId;

    public DiagnosisRequest() {}
    public DiagnosisRequest(String officialDiagnosis, String description, String followUps, String mreTitle, Long patientId) {
        this.officialDiagnosis = officialDiagnosis;
        this.description = description;
        this.followUps = followUps;
        this.mreTitle = mreTitle;
        this.patientId = patientId;
    }
    public String getOfficialDiagnosis() { return officialDiagnosis; }
    public void setOfficialDiagnosis(String officialDiagnosis) { this.officialDiagnosis = officialDiagnosis; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getFollowUps() { return followUps; }
    public void setFollowUps(String followUps) { this.followUps = followUps; }
    public String getMreTitle() { return mreTitle; }
    public void setMreTitle(String mreTitle) { this.mreTitle = mreTitle; }
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
}