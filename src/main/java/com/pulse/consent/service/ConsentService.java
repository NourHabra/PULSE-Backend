package com.pulse.consent.service;

import com.pulse.consent.model.*;
import com.pulse.consent.repository.ConsentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
@Service
public class ConsentService {

    private final ConsentRepository repo;

    public ConsentService(ConsentRepository repo) { this.repo = repo; }

    @Transactional
    public Consent giveConsent(Long patientId, Long doctorId) {
        return repo.findByPatientIdAndDoctorId(patientId, doctorId)
                .map(this::reactivate)
                .orElseGet(() -> create(patientId, doctorId));
    }


    /* Doctor creates request */
    @Transactional
    public Consent requestConsent(Long patientId, Long doctorId) {

        return repo.findByPatientIdAndDoctorId(patientId, doctorId)
                .filter(c -> c.getStatus() == ConsentStatus.REVOKED
                        || c.getStatus() == ConsentStatus.REJECTED)
                .map(c -> {
                    c.setStatus(ConsentStatus.PENDING);
                    c.setRequestedAt(Instant.now());
                    return repo.save(c);
                })
                .orElseGet(() -> repo.save(
                        new Consent(patientId, doctorId,
                                ConsentStatus.PENDING, Instant.now())
                ));
    }


    @Transactional
    public Consent decide(Long consentId, Long patientId, boolean approve) {

        Consent c = repo.findById(consentId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Consent " + consentId + " not found"));

        if (!c.getPatientId().equals(patientId) || c.getStatus() != ConsentStatus.PENDING) {
            throw new IllegalStateException("Not allowed");
        }

        c.setStatus(approve ? ConsentStatus.ACTIVE : ConsentStatus.REJECTED);
        c.setRequestedAt(Instant.now());
        return repo.save(c);
    }





    private Consent reactivate(Consent c) {
        c.setStatus(ConsentStatus.ACTIVE);
        c.setRequestedAt(Instant.now());
        return repo.save(c);
    }

    private Consent create(Long patientId, Long doctorId) {
        return repo.save(new Consent(
                patientId,
                doctorId,
                ConsentStatus.ACTIVE,
                Instant.now()
        ));
    }

}
