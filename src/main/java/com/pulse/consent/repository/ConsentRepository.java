package com.pulse.consent.repository;

import com.pulse.consent.model.Consent;
import com.pulse.consent.model.ConsentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ConsentRepository extends JpaRepository<Consent, Long> {

    Optional<Consent> findByPatientIdAndDoctorId(Long patientId, Long doctorId);

    boolean existsByPatientIdAndDoctorIdAndStatus(
            Long patientId, Long doctorId, ConsentStatus status);
}
