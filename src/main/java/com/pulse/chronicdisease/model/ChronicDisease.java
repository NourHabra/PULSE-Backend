package com.pulse.chronicdisease.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "chronic_disease")
public class ChronicDisease {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chronicDiseaseId;

    private String disease;
    private String type;

    public Long getChronicDiseaseId() {
        return chronicDiseaseId;
    }

    public void setChronicDiseaseId(Long chronicDiseaseId) {
        this.chronicDiseaseId = chronicDiseaseId;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}