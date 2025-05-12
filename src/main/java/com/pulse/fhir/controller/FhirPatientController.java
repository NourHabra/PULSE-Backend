package com.pulse.fhir.controller;

import com.pulse.fhir.service.FhirPatientService;
import com.pulse.fhir.mapper.PatientFhirMapper;
import com.pulse.user.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fhir/Patient")
public class FhirPatientController {

    private final FhirPatientService fhirPatientService;

    @Autowired
    public FhirPatientController(FhirPatientService fhirPatientService) {
        this.fhirPatientService = fhirPatientService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<org.hl7.fhir.r4.model.Patient> getPatient(@PathVariable String id) {
        Patient patient = fhirPatientService.getFromFhir(id);
        org.hl7.fhir.r4.model.Patient fhirPatient = PatientFhirMapper.toFhir(patient);
        return ResponseEntity.ok(fhirPatient);
    }

    @PostMapping
    public ResponseEntity<org.hl7.fhir.r4.model.Patient> createPatient(
            @RequestBody org.hl7.fhir.r4.model.Patient fhirPatient) {
        Patient patient = PatientFhirMapper.fromFhir(fhirPatient);
        fhirPatientService.pushToFhir(patient);
        return ResponseEntity.ok(fhirPatient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<org.hl7.fhir.r4.model.Patient> updatePatient(@PathVariable String id,
            @RequestBody org.hl7.fhir.r4.model.Patient fhirPatient) {
        fhirPatient.setId(id);
        Patient patient = PatientFhirMapper.fromFhir(fhirPatient);
        fhirPatientService.pushToFhir(patient);
        return ResponseEntity.ok(fhirPatient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable String id) {
        fhirPatientService.deleteFromFhir(id);
        return ResponseEntity.noContent().build();
    }
}