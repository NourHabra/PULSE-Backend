package com.pulse.vital.dto;


import java.time.LocalDateTime;

public class PatientVitalDto {
    private Long id;
    private Long patientId;
    private Long vitalId;
    private String name;
    private String description;
    private String normalValue;
    private String measurement;
    private LocalDateTime timestamp;

    public PatientVitalDto() {}

    public PatientVitalDto(Long id, Long patientId, Long vitalId,
                           String name, String description, String normalValue,
                           String measurement, LocalDateTime timestamp) {
        this.id = id;
        this.patientId = patientId;
        this.vitalId = vitalId;
        this.name = name;
        this.description = description;
        this.normalValue = normalValue;
        this.measurement = measurement;
        this.timestamp = timestamp;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public Long getVitalId() { return vitalId; }
    public void setVitalId(Long vitalId) { this.vitalId = vitalId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getNormalValue() { return normalValue; }
    public void setNormalValue(String normalValue) { this.normalValue = normalValue; }

    public String getMeasurement() { return measurement; }
    public void setMeasurement(String measurement) { this.measurement = measurement; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}