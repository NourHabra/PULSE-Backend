package com.pulse.labresult.controller;


import com.pulse.diagnosis.model.Diagnosis;
import com.pulse.labresult.model.LabResult;
import com.pulse.labresult.dto.LabResultRequest;
import com.pulse.labresult.service.LabResultService;
import com.pulse.medicalrecord.model.MedicalRecordEntry;
import com.pulse.medicalrecord.service.MedicalRecordEntryService;
import com.pulse.laboratory.model.Laboratory;
import com.pulse.laboratory.repository.LaboratoryRepository;
import com.pulse.test.model.Test;
import com.pulse.test.repository.TestRepository;
import com.pulse.user.model.LabTechnician;
import com.pulse.user.model.Patient;
import com.pulse.user.repository.LabTechnicianRepository;
import com.pulse.security.service.JwtService;
import com.pulse.util.FileStorageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/lab-results")
public class LabResultController {
    private final LabResultService service;
    private final MedicalRecordEntryService mreService;
    private final LaboratoryRepository labRepo;
    private final TestRepository testRepo;
    private final LabTechnicianRepository techRepo;
    private final JwtService jwtService;

    public LabResultController(LabResultService service,
                               MedicalRecordEntryService mreService,
                               LaboratoryRepository labRepo,
                               TestRepository testRepo,
                               LabTechnicianRepository techRepo,
                               JwtService jwtService) {
        this.service = service;
        this.mreService = mreService;
        this.labRepo = labRepo;
        this.testRepo = testRepo;
        this.techRepo = techRepo;
        this.jwtService = jwtService;
    }


    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<LabResult>> byPatient(@PathVariable Long patientId) {
        List<LabResult> list = service.findAllByPatientId(patientId);
        return ResponseEntity.ok(list);
    }
    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/patient/me")
    public ResponseEntity<List<LabResult>> listMyResults(@RequestHeader("Authorization") String authHeader) {

        String token = authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : authHeader;

        UserDetails userDetails = jwtService.getUserFromToken(token);
        if (!jwtService.isTokenValid(token, userDetails)) {
            throw new RuntimeException("Invalid or expired JWT");
        }
        if (userDetails instanceof Patient patient)
        {
            List<LabResult> list = service.findAllByPatientId(patient.getUserId());
            return ResponseEntity.ok(list);
        }


        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabResult> byId(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }


//    @PostMapping("/add")
//    public ResponseEntity<LabResult> addLabResult(
//            @RequestHeader("Authorization") String authHeader,
//            @RequestBody LabResultRequest dto
//    ) {
//
//        String token = authHeader.startsWith("Bearer ")
//                ? authHeader.substring(7)
//                : authHeader;
//        UserDetails ud = jwtService.getUserFromToken(token);
//        if (!jwtService.isTokenValid(token, ud)) {
//            throw new RuntimeException("Invalid/expired token");
//        }
//
//        boolean isTech = ud.getAuthorities().stream()
//                .anyMatch(a -> a.getAuthority().equals("ROLE_LAB_TECHNICIAN"));
//        if (!isTech) throw new AccessDeniedException("Not a lab technician");
//
//
//        LabTechnician tech = techRepo.findByEmail(ud.getUsername());
//        if (tech == null) throw new AccessDeniedException("Unknown technician");
//
//
//        Laboratory lab = labRepo.findById(dto.getLabId())
//                .orElseThrow(() -> new RuntimeException("No lab " + dto.getLabId()));
//        Test test = testRepo.findById(dto.getTestId())
//                .orElseThrow(() -> new RuntimeException("No test " + dto.getTestId()));
//
//
//        MedicalRecordEntry mre = new MedicalRecordEntry();
//        mre.setTitle(dto.getMreTitle());
//        mre.setTimestamp(LocalDateTime.now());
//        Patient p = new Patient(); p.setUserId(dto.getPatientId());
//        mre.setPatient(p);
//
//
//        LabResult lr = new LabResult();
//        lr.setLaboratory(lab);
//        lr.setTest(test);
//        lr.setTechnician(tech);
//        lr.setStatus(dto.getStatus());
//        lr.setResultsAttachment(dto.getResultsAttachment());
//        lr.setTechnicianNotes(dto.getTechnicianNotes());
//        lr.setMedicalRecordEntry(mre);
//
//        LabResult saved = service.createWithMre(lr, mre);
//
//        return ResponseEntity.ok(saved);
//    }

    @PostMapping("/add")
    public ResponseEntity<LabResult> addLabResult(
            @RequestHeader("Authorization") String authHeader,
            @RequestPart("data") LabResultRequest dto,
            @RequestPart("resultsAttachment") MultipartFile resultsAttachmentFile
    ) throws IOException {

        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        UserDetails ud = jwtService.getUserFromToken(token);
        if (!jwtService.isTokenValid(token, ud)) {
            throw new RuntimeException("Invalid/expired token");
        }

        boolean isTech = ud.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_LAB_TECHNICIAN"));
        if (!isTech) throw new AccessDeniedException("Not a lab technician");

        LabTechnician tech = techRepo.findByEmail(ud.getUsername());
        if (tech == null) throw new AccessDeniedException("Unknown technician");

        Laboratory lab = labRepo.findById(dto.getLabId())
                .orElseThrow(() -> new RuntimeException("No lab " + dto.getLabId()));
        Test test = testRepo.findById(dto.getTestId())
                .orElseThrow(() -> new RuntimeException("No test " + dto.getTestId()));

        String resultsAttachmentPath = FileStorageUtil.saveFile(resultsAttachmentFile, "lab_results");
        dto.setResultsAttachment(resultsAttachmentPath);

        MedicalRecordEntry mre = new MedicalRecordEntry();
        mre.setTitle(dto.getMreTitle());
        mre.setTimestamp(LocalDateTime.now());
        Patient p = new Patient();
        p.setUserId(dto.getPatientId());
        mre.setPatient(p);

        LabResult lr = new LabResult();
        lr.setLaboratory(lab);
        lr.setTest(test);
        lr.setTechnician(tech);
        lr.setStatus(dto.getStatus());
        lr.setResultsAttachment(resultsAttachmentPath);
        lr.setTechnicianNotes(dto.getTechnicianNotes());
        lr.setMedicalRecordEntry(mre);

        LabResult saved = service.createWithMre(lr, mre);

        return ResponseEntity.ok(saved);
    }

}
