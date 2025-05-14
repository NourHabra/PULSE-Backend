package com.pulse.fhir.controller;

import com.pulse.fhir.service.FhirPatientService;
import com.pulse.user.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fhir/patients")
public class FhirPatientController {

    private final FhirPatientService fhirPatientService;

    @Autowired
    public FhirPatientController(FhirPatientService fhirPatientService) {
        this.fhirPatientService = fhirPatientService;
    }

    @PostMapping(consumes = "application/fhir+json", produces = "application/fhir+json")
    public ResponseEntity<org.hl7.fhir.r4.model.Patient> createPatient(
            @RequestBody org.hl7.fhir.r4.model.Patient fhirPatient) {
        // Convert FHIR Patient to domain Patient
        Patient patient = fhirPatientService.fromFhir(fhirPatient);

        // Save the patient (this will generate the ID)
        Patient savedPatient = fhirPatientService.savePatient(patient);

        // Convert back to FHIR Patient with the generated ID
        org.hl7.fhir.r4.model.Patient responsePatient = fhirPatientService.pushToFhir(savedPatient);

        return ResponseEntity.ok(responsePatient);
    }
}