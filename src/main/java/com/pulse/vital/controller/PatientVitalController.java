package com.pulse.vital.controller;


import com.pulse.allergy.dto.PatientAllergyDto;
import com.pulse.consent.security.ConsentGuard;
import com.pulse.security.service.JwtService;
import com.pulse.user.model.Patient;
import com.pulse.vital.repository.PatientVitalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

import com.pulse.vital.dto.PatientVitalDto;
import com.pulse.vital.model.PatientVital;
import com.pulse.vital.service.PatientVitalService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/testing/patient-vitals")
public class PatientVitalController {
    @Autowired
    private PatientVitalService service;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ConsentGuard consentGuard;
    @Autowired
    private PatientVitalRepository patientVitalRepository;

    @GetMapping("/me")
    public ResponseEntity<List<PatientVitalDto>> getMine(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : authHeader;

        UserDetails user = jwtService.getUserFromToken(token);

        if (user instanceof Patient patient) {

            List<PatientVitalDto> result = service.getByPatientId(patient.getUserId());

            return ResponseEntity.ok(result);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PreAuthorize("@consentGuard.canRead(#patientId)")
    @GetMapping("/patient/{patientId}")
    public List<PatientVitalDto> getByPatient(@PathVariable Long patientId) {
        return service.getByPatientId(patientId);
    }


    @PostMapping
    public PatientVitalDto create(@RequestBody PatientVital pv) {
        Long patientId = pv.getPatient().getUserId();
        if (!consentGuard.canRead(patientId)) {
            throw new AccessDeniedException("You are not authorized to see this data");
        }

        return service.create(pv);
    }
    @PutMapping("/{id}")
    public PatientVitalDto update(@PathVariable Long id, @RequestBody PatientVital pv) {
        PatientVital existing = patientVitalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PatientVital not found"));

        Long patientId = existing.getPatient().getUserId();

        if (!consentGuard.canRead(patientId)) {
            throw new AccessDeniedException("You are not authorized to see this data");
        }

        existing.setMeasurement(pv.getMeasurement());
        existing.setTimestamp(pv.getTimestamp());
        if (pv.getVital() != null) {
            existing.setVital(pv.getVital());
        }
        patientVitalRepository.save(existing);
        return service.update(id, pv);
    }






    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {

        Optional<PatientVital> optPv = patientVitalRepository.findById(id);

        PatientVital pv = optPv.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "PatientVital not found"));

        Long patientId = pv.getPatient().getUserId();

        if (!consentGuard.canRead(patientId)) {
            throw new AccessDeniedException("You are not authorized to see this data");
        }

        service.delete(id);
    }

}
