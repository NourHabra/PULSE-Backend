package com.pulse.diagnosis.controller;


import com.pulse.diagnosis.dto.DiagnosisRequest;
import com.pulse.diagnosis.model.Diagnosis;
import com.pulse.diagnosis.service.DiagnosisService;
import com.pulse.medicalrecord.model.MedicalRecordEntry;
import com.pulse.medicalrecord.service.MedicalRecordEntryService;
import com.pulse.user.model.Doctor;
import com.pulse.user.model.Patient;
import com.pulse.user.repository.DoctorRepository;
import com.pulse.security.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
            @RequestBody DiagnosisRequest dto
    ) {
        // 1) extract & validate token
        String token = authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : authHeader;
        UserDetails userDetails = jwtService.getUserFromToken(token);
        if (!jwtService.isTokenValid(token, userDetails)) {
            throw new RuntimeException("Invalid or expired JWT");
        }

        // 2) ensure ROLE_DOCTOR
        boolean isDoctor = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"));
        if (!isDoctor) {
            throw new AccessDeniedException("You are not a Doctor");
        }

        // 3) lookup Doctor entity
        String username = userDetails.getUsername();
        Doctor doctor = doctorRepo.findByEmail(username);
        if (doctor == null) {
            throw new AccessDeniedException("You are not registered as a Doctor");
        }

        // 4) build the MRE
        MedicalRecordEntry mre = new MedicalRecordEntry();
        mre.setTitle(dto.getMreTitle());
        mre.setTimestamp(LocalDateTime.now());
        Patient patient = new Patient();
        patient.setUserId(dto.getPatientId());
        mre.setPatient(patient);

        // 5) build Diagnosis
        Diagnosis diag = new Diagnosis();
        diag.setOfficialDiagnosis(dto.getOfficialDiagnosis());
        diag.setDescription(dto.getDescription());
        diag.setFollowUps(dto.getFollowUps());
        diag.setDoctor(doctor);

        // 6) save both under one transaction
        Diagnosis saved = diagnosisService.createDiagnosisWithMre(diag, mre);

        logger.info("Diagnosis created id={} by doctor={}",
                saved.getDiagnosisId(),
                doctor.getUserId());
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Diagnosis>> listByPatient(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long patientId
    ) {
        // validate JWT…
        List<Diagnosis> list = diagnosisService.findAllByPatientId(patientId);
        return ResponseEntity.ok(list);
    }
}