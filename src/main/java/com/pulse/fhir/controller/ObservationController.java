package com.pulse.fhir.controller;

import ca.uhn.fhir.context.FhirContext;

import com.pulse.fhir.mapper.ObservationMapper;
import com.pulse.vital.model.PatientVital;
import com.pulse.vital.repository.PatientVitalRepository;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Observation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fhir/Observation")
public class ObservationController {

    private final PatientVitalRepository patientVitalRepository;
    private final FhirContext fhirContext;

    public ObservationController(PatientVitalRepository patientVitalRepository, FhirContext fhirContext) {
        this.patientVitalRepository = patientVitalRepository;
        this.fhirContext = fhirContext;
    }

    @GetMapping(value = "/{patientId}", produces = "application/fhir+json")
    public ResponseEntity<String> getPatientObservations(@PathVariable Long patientId) {
        List<PatientVital> vitals = patientVitalRepository.findByPatient_UserId(patientId);

        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.SEARCHSET);
        for (PatientVital vital : vitals) {
            Observation obs = ObservationMapper.toFhirObservation(vital);
            bundle.addEntry().setResource(obs);
        }

        String bundleJson = fhirContext.newJsonParser().encodeResourceToString(bundle);
        return ResponseEntity.ok(bundleJson);
    }
}
