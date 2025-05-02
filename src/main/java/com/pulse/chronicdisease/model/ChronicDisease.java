package com.pulse.chronicdisease.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chronic_disease")
public class ChronicDisease {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chronicDiseaseId;

    private String disease;
    private String type;
}