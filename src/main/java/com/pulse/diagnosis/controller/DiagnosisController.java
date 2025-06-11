package com.pulse.diagnosis.controller;


import com.pulse.diagnosis.dto.DiagnosisRequest;
import com.pulse.diagnosis.model.Diagnosis;
import com.pulse.diagnosis.service.DiagnosisService;
import com.pulse.emergencyevent.model.EmergencyEvent;
import com.pulse.medicalrecord.model.MedicalRecordEntry;
import com.pulse.medicalrecord.service.MedicalRecordEntryService;
import com.pulse.user.model.Doctor;
import com.pulse.user.model.Patient;
import com.pulse.user.repository.DoctorRepository;
import com.pulse.security.service.JwtService;
import com.pulse.util.FileStorageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
@RestController
@RequestMapping("/diagnosis")
public class DiagnosisController {

    private final DiagnosisService diagnosisService;
    private final JwtService jwtService;
    private final DoctorRepository doctorRepo;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public DiagnosisController(DiagnosisService diagnosisService,
                               JwtService jwtService,
                               DoctorRepository doctorRepo) {
        this.diagnosisService = diagnosisService;
        this.jwtService = jwtService;
        this.doctorRepo = doctorRepo;
    }

    @PostMapping("/add")
    public ResponseEntity<Diagnosis> addDiagnosis(
            @RequestHeader("Authorization") String authHeader,
            @RequestPart("data") DiagnosisRequest dto,
            @RequestPart("attachment") MultipartFile attachmentFile
    ) throws IOException {

        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        UserDetails userDetails = jwtService.getUserFromToken(token);
        if (!jwtService.isTokenValid(token, userDetails)) {
            throw new RuntimeException("Invalid or expired JWT");
        }

        boolean isDoctor = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"));
        if (!isDoctor) {
            throw new AccessDeniedException("You are not a Doctor");
        }

        String username = userDetails.getUsername();
        Doctor doctor = doctorRepo.findByEmail(username);
        if (doctor == null) {
            throw new AccessDeniedException("You are not registered as a Doctor");
        }

        String attachmentPath = FileStorageUtil.saveFile(attachmentFile, "diagnosis_attachment");
        dto.setAttachment(attachmentPath);

        MedicalRecordEntry mre = new MedicalRecordEntry();
        mre.setTitle(dto.getMreTitle());
        mre.setTimestamp(LocalDateTime.now());
        Patient patient = new Patient();
        patient.setUserId(dto.getPatientId());
        mre.setPatient(patient);

        Diagnosis diag = new Diagnosis();
        diag.setDescription(dto.getDescription());
        diag.setICD10(dto.getICD10());
        diag.setAttachment(attachmentPath);
        diag.setDoctor(doctor);
        diag.setMedicalRecordEntry(mre);

        Diagnosis saved = diagnosisService.createDiagnosisWithMre(diag, mre);

        logger.info("Diagnosis created id={} by doctor={}", saved.getDiagnosisId(), doctor.getUserId());
        return ResponseEntity.ok(saved);
    }



    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Diagnosis>> listByPatient(
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
        List<Diagnosis> list = diagnosisService.findAllByPatientId(patientId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Diagnosis> getById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long eventId) {

        String token = authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : authHeader;

        UserDetails userDetails = jwtService.getUserFromToken(token);
        if (!jwtService.isTokenValid(token, userDetails)) {
            throw new RuntimeException("Invalid or expired JWT");
        }


        Diagnosis diagnosis = diagnosisService.findById(eventId);
        if (diagnosis == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(diagnosis);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/patient/me")
    public ResponseEntity<List<Diagnosis>> listMyDiagnosis(@RequestHeader("Authorization") String authHeader) {

        String token = authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : authHeader;

        UserDetails userDetails = jwtService.getUserFromToken(token);
        if (!jwtService.isTokenValid(token, userDetails)) {
            throw new RuntimeException("Invalid or expired JWT");
        }
        if (userDetails instanceof Patient patient)
        {
            List<Diagnosis> list = diagnosisService.findAllByPatientId(patient.getUserId());
            return ResponseEntity.ok(list);
        }


        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}