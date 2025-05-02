package com.pulse.chronicdisease.dto;


import java.time.LocalDate;

public class PatientChronicDiseaseDto {
    private Long id;
    private Long patientId;
    private Long chronicDiseaseId;
    private String disease;
    private String type;
    private String intensity;
    private LocalDate startDate;

    public PatientChronicDiseaseDto() {}

    public PatientChronicDiseaseDto(Long id, Long patientId, Long chronicDiseaseId,
                                    String disease, String type,
                                    String intensity, LocalDate startDate) {
        this.id = id;
        this.patientId = patientId;
        this.chronicDiseaseId = chronicDiseaseId;
        this.disease = disease;
        this.type = type;
        this.intensity = intensity;
        this.startDate = startDate;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public Long getChronicDiseaseId() { return chronicDiseaseId; }
    public void setChronicDiseaseId(Long chronicDiseaseId) { this.chronicDiseaseId = chronicDiseaseId; }

    public String getDisease() { return disease; }
    public void setDisease(String disease) { this.disease = disease; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getIntensity() { return intensity; }
    public void setIntensity(String intensity) { this.intensity = intensity; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
}
