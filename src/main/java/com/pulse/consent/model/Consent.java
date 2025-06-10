package com.pulse.consent.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
        name = "consent",
        uniqueConstraints = @UniqueConstraint(columnNames = {"patient_id", "doctor_id"})
)
public class Consent {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Enumerated(EnumType.STRING)
    private ConsentStatus status;

    private Instant requestedAt;


    public Consent() {}

    public Consent(Long patientId, Long doctorId,
                   ConsentStatus status, Instant requestedAt) {
        this.patientId   = patientId;
        this.doctorId    = doctorId;
        this.status      = status;
        this.requestedAt = requestedAt;
    }


    public Long getId()              { return id; }
    public Long getPatientId()       { return patientId; }
    public void setPatientId(Long v) { this.patientId = v; }
    public Long getDoctorId()        { return doctorId; }
    public void setDoctorId(Long v)  { this.doctorId = v; }
    public ConsentStatus getStatus() { return status; }
    public void setStatus(ConsentStatus v) { this.status = v; }
    public Instant getRequestedAt()  { return requestedAt; }
    public void setRequestedAt(Instant v)  { this.requestedAt = v; }
}
