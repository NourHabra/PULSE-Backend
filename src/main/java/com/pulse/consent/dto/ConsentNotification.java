package com.pulse.consent.dto;

public class ConsentNotification {

    private String type;
    private Long   consentId;
    private Long   doctorId;
    private Long   patientId;
    private String status;

    public ConsentNotification() { }

    public String getType()      { return type; }
    public Long   getConsentId() { return consentId; }
    public Long   getDoctorId()  { return doctorId; }
    public Long   getPatientId() { return patientId; }
    public String getStatus()    { return status; }

    public void setType(String type)             { this.type = type; }
    public void setConsentId(Long consentId)     { this.consentId = consentId; }
    public void setDoctorId(Long doctorId)       { this.doctorId = doctorId; }
    public void setPatientId(Long patientId)     { this.patientId = patientId; }
    public void setStatus(String status)         { this.status = status; }
}
