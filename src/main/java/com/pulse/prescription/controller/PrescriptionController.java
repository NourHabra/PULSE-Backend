package com.pulse.prescription.controller;


import com.pulse.prescription.dto.PrescriptionRequest;
import com.pulse.prescription.model.Prescription;
import com.pulse.prescription.service.PrescriptionService;
import com.pulse.user.model.Doctor;
import com.pulse.user.model.Patient;
import com.pulse.user.repository.DoctorRepository;
import com.pulse.security.service.JwtService;
import com.pulse.medicalrecord.model.MedicalRecordEntry;

import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/prescription")
public class PrescriptionController {

    private final PrescriptionService service;
    private final JwtService jwtService;
    private final DoctorRepository doctorRepo;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public PrescriptionController(PrescriptionService service,
                                  JwtService jwtService,
                                  DoctorRepository doctorRepo) {
        this.service = service;
        this.jwtService = jwtService;
        this.doctorRepo = doctorRepo;
    }

    @PostMapping("/add")
    public ResponseEntity<Prescription> addPrescription(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody PrescriptionRequest dto) {

        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        UserDetails userDetails = jwtService.getUserFromToken(token);
        if (!jwtService.isTokenValid(token, userDetails)) throw new RuntimeException("Invalid or expired JWT");

        boolean isDoctor = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"));
        if (!isDoctor) throw new AccessDeniedException("You are not a Doctor");

                Doctor doctor = doctorRepo.findByEmail(userDetails.getUsername());
        if (doctor == null) throw new AccessDeniedException("Not registered as a Doctor");

                MedicalRecordEntry mre = new MedicalRecordEntry();
        mre.setTitle(dto.getMreTitle());
        mre.setTimestamp(LocalDateTime.now());
        com.pulse.user.model.Patient patient = new com.pulse.user.model.Patient();
        patient.setUserId(dto.getPatientId());
        mre.setPatient(patient);

        Prescription presc = new Prescription();
        presc.setNotes(dto.getNotes());
        presc.setStatus(dto.getStatus());
        presc.setDoctor(doctor);

        Prescription saved = service.createWithMre(presc, mre);
        logger.info("Prescription created id={} by doctor={}", saved.getPrescriptionId(), doctor.getUserId());
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/patient/{patientId}")
            public ResponseEntity<List<Prescription>> listByPatient(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long patientId) {

        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
                UserDetails userDetails = jwtService.getUserFromToken(token);
        if (!jwtService.isTokenValid(token, userDetails)) throw new RuntimeException("Invalid or expired JWT");

                List<Prescription> list = service.findAllByPatientId(patientId);
        return ResponseEntity.ok(list);
    }



    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/patient/me")
    public ResponseEntity<List<Prescription>> listMyPrescription( @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : authHeader;

        UserDetails userDetails = jwtService.getUserFromToken(token);
        if (!jwtService.isTokenValid(token, userDetails)) {
            throw new RuntimeException("Invalid or expired JWT");
        }
        if (userDetails instanceof Patient patient)
        {
            List<Prescription> list = service.findAllByPatientId(patient.getUserId());
            return ResponseEntity.ok(list);
        }


        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}