package com.pulse.allergy.model;


import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import com.pulse.user.model.Patient;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patient_allergy")
public class PatientAllergy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "allergy_id", nullable = false)
    private Allergy allergy;

    private String intensity;
}