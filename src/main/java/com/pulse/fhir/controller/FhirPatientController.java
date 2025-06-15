package com.pulse.fhir.controller;

import com.pulse.fhir.mapper.PatientFhirMapper;

import com.pulse.user.model.Patient;
import com.pulse.user.repository.PatientRepository;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.hl7.fhir.r4.model.Bundle;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ca.uhn.fhir.context.FhirContext;

import org.springframework.http.MediaType;

@RestController
@RequestMapping("/fhir/Patient")
public class FhirPatientController {

    private final PatientRepository patientRepository;
    private final FhirContext fhirContext;


    public FhirPatientController(PatientRepository patientRepository, FhirContext fhirContext) {
        this.patientRepository = patientRepository;

        this.fhirContext = fhirContext;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPatientFhir(@PathVariable Long id) {
        Optional<Patient> patientOpt = patientRepository.findById(id);
        if (patientOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Patient patient = patientOpt.get();

        org.hl7.fhir.r4.model.Patient fhirPatient = PatientFhirMapper.toFhir(patient);

        String json = fhirContext.newJsonParser().encodeResourceToString(fhirPatient);

        return ResponseEntity.ok(json);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        List<org.hl7.fhir.r4.model.Patient> fhirPatients = patients.stream()
                .map(PatientFhirMapper::toFhir)
                .collect(Collectors.toList());

        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.SEARCHSET);
        for (org.hl7.fhir.r4.model.Patient p : fhirPatients) {
            bundle.addEntry().setResource(p);
        }

        String json = fhirContext.newJsonParser().encodeResourceToString(bundle);
        return ResponseEntity.ok(json);
    }

}
