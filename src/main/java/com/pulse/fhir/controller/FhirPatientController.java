package com.pulse.fhir.controller;

import com.pulse.fhir.service.FhirPatientService;
import com.pulse.user.model.Patient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fhir/patients")
public class FhirPatientController {
    private final FhirPatientService fhirPatientService;

    public FhirPatientController(FhirPatientService fhirPatientService) {
        this.fhirPatientService = fhirPatientService;
    }

    @PostMapping
    public ResponseEntity<org.hl7.fhir.r4.model.Patient> createPatient(
            @RequestBody org.hl7.fhir.r4.model.Patient fhirPatient) {
        Patient patient = fhirPatientService.fromFhir(fhirPatient);
        org.hl7.fhir.r4.model.Patient validatedPatient = fhirPatientService.pushToFhir(patient);
        return ResponseEntity.ok(validatedPatient);
    }
}