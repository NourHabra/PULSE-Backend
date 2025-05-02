package com.pulse.medicalrecord.controller;


import com.pulse.medicalrecord.model.MedicalRecordEntry;
import com.pulse.medicalrecord.service.MedicalRecordEntryService;
import com.pulse.security.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mre")
public class MedicalRecordEntryController {
    private final MedicalRecordEntryService service;
    private final JwtService jwtService;

    private final MedicalRecordEntryService mreService;


    public MedicalRecordEntryController(MedicalRecordEntryService service,JwtService jwtService,MedicalRecordEntryService mreService) {
        this.service = service;
        this.jwtService=jwtService;
        this.mreService = mreService;
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalRecordEntry>> listMre(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long patientId
    ) {

        String token = authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : authHeader;
        UserDetails userDetails = jwtService.getUserFromToken(token);
        if (!jwtService.isTokenValid(token, userDetails)) {
            throw new RuntimeException("Invalid or expired JWT");
        }
        List<MedicalRecordEntry> list = mreService.findAllByPatientId(patientId);
        return ResponseEntity.ok(list);
    }
}

