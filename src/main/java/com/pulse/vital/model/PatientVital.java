package com.pulse.vital.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.io.Serializable;
import com.pulse.user.model.Patient;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patient_vital")
public class PatientVital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vital_id", nullable = false)
    private Vital vital;

    private String measurement;
    private LocalDateTime timestamp;
}
