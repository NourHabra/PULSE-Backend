package com.pulse.allergy.dto;
public class PatientAllergyDto {

    private Long id;
    private Long patientId;
    private Long allergyId;
    private String allergen;
    private String type;
    private String intensity;

    // No-args constructor
    public PatientAllergyDto() {
    }

    // All-args constructor
    public PatientAllergyDto(Long id, Long patientId, Long allergyId, String allergen, String type, String intensity) {
        this.id = id;
        this.patientId = patientId;
        this.allergyId = allergyId;
        this.allergen = allergen;
        this.type = type;
        this.intensity = intensity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getAllergyId() {
        return allergyId;
    }

    public void setAllergyId(Long allergyId) {
        this.allergyId = allergyId;
    }

    public String getAllergen() {
        return allergen;
    }

    public void setAllergen(String allergen) {
        this.allergen = allergen;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIntensity() {
        return intensity;
    }

    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }
}