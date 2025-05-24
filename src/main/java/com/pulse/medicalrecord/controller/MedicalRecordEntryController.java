package com.pulse.medicalrecord.controller;


import com.pulse.medicalrecord.model.MedicalRecordEntry;
import com.pulse.medicalrecord.service.MedicalRecordEntryService;
import com.pulse.security.service.JwtService;
import com.pulse.user.model.Patient;
import com.pulse.vital.dto.PatientVitalDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mre")
public class MedicalRecordEntryController {

    private final JwtService jwtService;

    private final MedicalRecordEntryService mreService;


    public MedicalRecordEntryController(JwtService jwtService,MedicalRecordEntryService mreService) {

        this.jwtService=jwtService;
        this.mreService = mreService;
    }


    @PreAuthorize("@consentGuard.canRead(#patientId)")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalRecordEntry>> listMre(@PathVariable Long patientId) {
        List<MedicalRecordEntry> list = mreService.findAllByPatientId(patientId);
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/patient/me")
    public ResponseEntity<List<MedicalRecordEntry>> listMyMre( @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : authHeader;

        UserDetails userDetails = jwtService.getUserFromToken(token);
        if (!jwtService.isTokenValid(token, userDetails)) {
            throw new RuntimeException("Invalid or expired JWT");
        }
        if (userDetails instanceof Patient patient)
        {
            List<MedicalRecordEntry> list = mreService.findAllByPatientId(patient.getUserId());
            return ResponseEntity.ok(list);
        }


        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}

