package com.pulse.chronicdisease.model;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.io.Serializable;
import com.pulse.user.model.Patient;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patient_chronic_disease")
public class PatientChronicDisease {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chronic_disease_id", nullable = false)
    private ChronicDisease chronicDisease;

    private String intensity;
    private LocalDate startDate;
}