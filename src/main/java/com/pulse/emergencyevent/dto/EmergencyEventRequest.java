package com.pulse.emergencyevent.dto;

import java.time.LocalDateTime;

public class EmergencyEventRequest {
    private String notes;
    private Long patientId;
    private String mreTitle;

    public EmergencyEventRequest() {
    }


    public EmergencyEventRequest(String notes, Long patientId, String mreTitle ) {
        this.notes = notes;
        this.patientId = patientId;
        this.mreTitle = mreTitle;

    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getMreTitle() {
        return mreTitle;
    }

    public void setMreTitle(String mreTitle) {
        this.mreTitle = mreTitle;
    }

}