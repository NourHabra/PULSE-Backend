package com.pulse.chronicdisease.controller;

import com.pulse.chronicdisease.repository.PatientChronicDiseaseRepository;
import com.pulse.security.service.JwtService;
import com.pulse.user.model.Patient;
import com.pulse.vital.dto.PatientVitalDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.pulse.chronicdisease.dto.PatientChronicDiseaseDto;
import com.pulse.chronicdisease.model.PatientChronicDisease;
import com.pulse.chronicdisease.service.PatientChronicDiseaseService;
import com.pulse.consent.security.ConsentGuard;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/testing/patient-chronic-diseases")
public class PatientChronicDiseaseController {
    @Autowired
    private PatientChronicDiseaseService service;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ConsentGuard consentGuard;
    @Autowired
    private PatientChronicDiseaseRepository patientChronicDiseaseRepository;

    @GetMapping("/me")
    public ResponseEntity<List<PatientChronicDiseaseDto>> getMine(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : authHeader;

        UserDetails user = jwtService.getUserFromToken(token);

        if (user instanceof Patient patient) {

            List<PatientChronicDiseaseDto> result = service.getByPatientId(patient.getUserId());

            return ResponseEntity.ok(result);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PreAuthorize("@consentGuard.canRead(#patientId)")
    @GetMapping("/patient/{patientId}")
    public List<PatientChronicDiseaseDto> getByPatient(@PathVariable Long patientId) {
        return service.getByPatientId(patientId);
    }


    @PostMapping
    public PatientChronicDiseaseDto create(@RequestBody PatientChronicDisease pcd) {
        Long patientId = pcd.getPatient().getUserId();
        if (!consentGuard.canRead(patientId)) {
            throw new AccessDeniedException("You are not authorized to see this data");
        }
        return service.create(pcd);
    }


    @PutMapping("/{id}")
    public PatientChronicDiseaseDto update(@PathVariable Long id, @RequestBody PatientChronicDisease pcd) {
        PatientChronicDisease existing = patientChronicDiseaseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found"));

        Long patientId = existing.getPatient().getUserId();
        if (!consentGuard.canRead(patientId)) {
            throw new AccessDeniedException("You are not authorized to see this data");
        }



        return service.update(id, pcd);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        PatientChronicDisease existing = patientChronicDiseaseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found"));

        Long patientId = existing.getPatient().getUserId();
        if (!consentGuard.canRead(patientId)) {
            throw new AccessDeniedException("You are not authorized to see this data");
        }
        service.delete(id);
    }

}