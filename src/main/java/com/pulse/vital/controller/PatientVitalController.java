package com.pulse.vital.controller;


import com.pulse.allergy.dto.PatientAllergyDto;
import com.pulse.security.service.JwtService;
import com.pulse.user.model.Patient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.pulse.vital.dto.PatientVitalDto;
import com.pulse.vital.model.PatientVital;
import com.pulse.vital.service.PatientVitalService;

@RestController
@RequestMapping("/testing/patient-vitals")
public class PatientVitalController {
    @Autowired
    private PatientVitalService service;
    @Autowired
    private JwtService jwtService;

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

    @GetMapping("/patient/{patientId}")
    public List<PatientVitalDto> getByPatient(@PathVariable Long patientId) {
        return service.getByPatientId(patientId);
    }

    @PostMapping
    public PatientVitalDto create(@RequestBody PatientVital pv) {
        return service.create(pv);
    }

    @PutMapping("/{id}")
    public PatientVitalDto update(@PathVariable Long id, @RequestBody PatientVital pv) {
        return service.update(id, pv);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
