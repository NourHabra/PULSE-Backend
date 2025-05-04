package com.pulse.allergy.controller;


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
@RestController
@RequestMapping("/testing/patient-allergies")
public class PatientAllergyController {

    @Autowired
    private PatientAllergyService service;
    @Autowired
    private  JwtService jwtService;

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


    @GetMapping("/patient/{patientId}")
    public List<PatientAllergyDto> getByPatient(@PathVariable Long patientId) {
        return service.getByPatientId(patientId);
    }


    @PostMapping
    public PatientAllergy create(@RequestBody PatientAllergy pa) {
        return service.create(pa);
    }



    @PutMapping("/{id}")
    public PatientAllergyDto update(@PathVariable Long id, @RequestBody PatientAllergy pa) {
        return service.update(id, pa);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}