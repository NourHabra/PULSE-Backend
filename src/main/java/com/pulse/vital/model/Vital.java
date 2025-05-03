package com.pulse.vital.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "vital")
public class Vital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vitalId;

    private String name;
    private String description;
    private String normalValue;

    public Long getVitalId() {
        return vitalId;
    }

    public void setVitalId(Long vitalId) {
        this.vitalId = vitalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNormalValue() {
        return normalValue;
    }

    public void setNormalValue(String normalValue) {
        this.normalValue = normalValue;
    }

}
