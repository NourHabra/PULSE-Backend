package com.pulse.allergy.controller;


import com.pulse.allergy.repository.PatientAllergyRepository;
import com.pulse.consent.security.ConsentGuard;
import com.pulse.security.service.JwtService;
import com.pulse.user.model.Patient;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.pulse.allergy.model.PatientAllergy;
import com.pulse.allergy.service.PatientAllergyService;
import com.pulse.allergy.dto.PatientAllergyDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;
@RestController
@RequestMapping("/testing/patient-allergies")
public class PatientAllergyController {

    @Autowired
    private PatientAllergyService service;
    @Autowired
    private  JwtService jwtService;
    @Autowired
    private ConsentGuard consentGuard;
    @Autowired
    private PatientAllergyRepository patientAllergyRepository;

    @GetMapping("/me")
    public ResponseEntity<List<PatientAllergyDto>> getMine(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : authHeader;

        UserDetails user = jwtService.getUserFromToken(token);

        if (user instanceof Patient patient) {

            List<PatientAllergyDto> result = service.getByPatientId(patient.getUserId());

            return ResponseEntity.ok(result);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


    @PreAuthorize("@consentGuard.canRead(#patientId)")
    @GetMapping("/patient/{patientId}")
    public List<PatientAllergyDto> getByPatient(@PathVariable Long patientId) {
        return service.getByPatientId(patientId);
    }



    @PostMapping
    public PatientAllergy create(@RequestBody PatientAllergy pa) {
        Long patientId = pa.getPatient().getUserId();
        if (!consentGuard.canRead(patientId)) {
            throw new AccessDeniedException("You are not authorized to see this data");
        }
        return service.create(pa);
    }



    @PutMapping("/{id}")
    public PatientAllergyDto update(@PathVariable Long id, @RequestBody PatientAllergy pa) {
        PatientAllergy existing = patientAllergyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PatientAllergy not found"));

        Long patientId = existing.getPatient().getUserId();
        if (!consentGuard.canRead(patientId)) {
            throw new AccessDeniedException("You are not authorized to see this data");
        }


        return service.update(id, pa);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        PatientAllergy existing = patientAllergyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PatientAllergy not found"));

        Long patientId = existing.getPatient().getUserId();
        if (!consentGuard.canRead(patientId)) {
            throw new AccessDeniedException("You are not authorized to see this data");
        }
        service.delete(id);
    }

}