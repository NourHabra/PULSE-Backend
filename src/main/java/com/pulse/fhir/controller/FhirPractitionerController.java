package com.pulse.fhir.controller;

import com.pulse.user.model.Doctor;
import com.pulse.user.model.LabTechnician;
import com.pulse.user.model.EmergencyWorker;
import com.pulse.user.model.HealthEmployee;
import com.pulse.fhir.mapper.PractitionerFhirMapper;

import com.pulse.user.repository.DoctorRepository;
import com.pulse.user.repository.LabTechnicianRepository;
import com.pulse.user.repository.EmergencyWorkerRepository;
import com.pulse.user.repository.HealthEmployeeRepository;
import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/fhir/Practitioner")
public class FhirPractitionerController {

    private final DoctorRepository doctorRepository;
    private final LabTechnicianRepository labTechnicianRepository;
    private final EmergencyWorkerRepository emergencyWorkerRepository;
    private final HealthEmployeeRepository healthEmployeeRepository;
    private final FhirContext fhirContext; // Added HAPI FHIR context

    public FhirPractitionerController(DoctorRepository doctorRepository,
                                      LabTechnicianRepository labTechnicianRepository,
                                      EmergencyWorkerRepository emergencyWorkerRepository,
                                      HealthEmployeeRepository healthEmployeeRepository,
                                      FhirContext fhirContext) {

        this.doctorRepository = doctorRepository;
        this.labTechnicianRepository = labTechnicianRepository;
        this.emergencyWorkerRepository = emergencyWorkerRepository;
        this.healthEmployeeRepository = healthEmployeeRepository;
        this.fhirContext = fhirContext;
    }

    // GET a single practitioner (Doctor, Lab Technician, Emergency Worker, Health Employee) by ID in FHIR format
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPractitioner(@PathVariable Long id) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(id);
        Optional<LabTechnician> labTechnicianOpt = labTechnicianRepository.findById(id);
        Optional<EmergencyWorker> emergencyWorkerOpt = emergencyWorkerRepository.findById(id);
        Optional<HealthEmployee> healthEmployeeOpt = healthEmployeeRepository.findById(id);

        if (doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            Practitioner practitioner = PractitionerFhirMapper.toFhir(doctor);

            // Serialize the FHIR Practitioner to JSON
            String json = fhirContext.newJsonParser().encodeResourceToString(practitioner);
            return ResponseEntity.ok(json);
        } else if (labTechnicianOpt.isPresent()) {
            LabTechnician labTechnician = labTechnicianOpt.get();
            Practitioner practitioner = PractitionerFhirMapper.toFhir(labTechnician);

            // Serialize the FHIR Practitioner to JSON
            String json = fhirContext.newJsonParser().encodeResourceToString(practitioner);
            return ResponseEntity.ok(json);
        } else if (emergencyWorkerOpt.isPresent()) {
            EmergencyWorker emergencyWorker = emergencyWorkerOpt.get();
            Practitioner practitioner = PractitionerFhirMapper.toFhir(emergencyWorker);

            // Serialize the FHIR Practitioner to JSON
            String json = fhirContext.newJsonParser().encodeResourceToString(practitioner);
            return ResponseEntity.ok(json);
        } else if (healthEmployeeOpt.isPresent()) {
            HealthEmployee healthEmployee = healthEmployeeOpt.get();
            Practitioner practitioner = PractitionerFhirMapper.toFhir(healthEmployee);

            // Serialize the FHIR Practitioner to JSON
            String json = fhirContext.newJsonParser().encodeResourceToString(practitioner);
            return ResponseEntity.ok(json);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // GET all practitioners (Doctor, Lab Technician, Emergency Worker, Health Employee) in FHIR format
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllPractitioners() {
        List<Doctor> doctors = doctorRepository.findAll();
        List<LabTechnician> labTechnicians = labTechnicianRepository.findAll();
        List<EmergencyWorker> emergencyWorkers = emergencyWorkerRepository.findAll();
        List<HealthEmployee> healthEmployees = healthEmployeeRepository.findAll();

        // Convert all practitioners to FHIR format
        List<Practitioner> fhirPractitioners = doctors.stream()
                .map(PractitionerFhirMapper::toFhir)
                .collect(Collectors.toList());
        labTechnicians.stream()
                .map(PractitionerFhirMapper::toFhir)
                .forEach(fhirPractitioners::add);
        emergencyWorkers.stream()
                .map(PractitionerFhirMapper::toFhir)
                .forEach(fhirPractitioners::add);
        healthEmployees.stream()
                .map(PractitionerFhirMapper::toFhir)
                .forEach(fhirPractitioners::add);

        // Wrap in a FHIR Bundle resource for proper FHIR response
        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.SEARCHSET);
        for (Practitioner practitioner : fhirPractitioners) {
            bundle.addEntry().setResource(practitioner);
        }

        // Serialize the FHIR Bundle to JSON
        String json = fhirContext.newJsonParser().encodeResourceToString(bundle);
        return ResponseEntity.ok(json);
    }
}
