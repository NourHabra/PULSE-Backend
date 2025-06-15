package com.pulse.consent.service;

import com.pulse.consent.model.*;
import com.pulse.consent.repository.ConsentRepository;
import com.pulse.exception.ConsentAlreadyPendingException;
import com.pulse.user.model.Patient;
import com.pulse.user.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ConsentService {

    private final ConsentRepository repo;
    private final PatientRepository patientRepository;


    public ConsentService(ConsentRepository repo, PatientRepository patientRepository) { this.repo = repo; this.patientRepository=patientRepository;}

    @Transactional
    public Consent giveConsent(Long patientId, Long doctorId) {
        return repo.findByPatientIdAndDoctorId(patientId, doctorId)
                .map(this::reactivate)
                .orElseGet(() -> create(patientId, doctorId));
    }


    @Transactional
    public Consent requestConsent(Long patientId, Long doctorId) {

        return repo.findByPatientIdAndDoctorId(patientId, doctorId)
                .map(c -> {


                    if (c.getStatus() == ConsentStatus.PENDING) {
                        throw new ConsentAlreadyPendingException(patientId);
                    }

                    if (c.getStatus() == ConsentStatus.REVOKED
                            || c.getStatus() == ConsentStatus.REJECTED) {

                        c.setStatus(ConsentStatus.PENDING);
                        c.setRequestedAt(Instant.now());
                        return repo.save(c);
                    }


                    return c;
                })
                .orElseGet(() ->           
                        repo.save(new Consent(
                                patientId,
                                doctorId,
                                ConsentStatus.PENDING,
                                Instant.now()
                        )));
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
    public List<Patient> getPatientsByDoctorId(Long doctorId) {
        List<Consent> consents = repo.findByDoctorId(doctorId);

        List<Long> patientIds = consents.stream()
                .map(Consent::getPatientId)
                .collect(Collectors.toList());

        return patientRepository.findAllById(patientIds);
    }

    public void deleteConsentByIdAndPatient(Long consentId, Long patientId) {
        Consent consent = repo.findById(consentId)
                .orElseThrow(() -> new RuntimeException("Consent not found"));

        if (!consent.getPatientId().equals(patientId)) {
            throw new RuntimeException("Unauthorized to delete this consent");
        }

        repo.deleteById(consentId);
    }

}
