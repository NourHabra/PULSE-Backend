package com.pulse.emergencyevent.controller;


import com.pulse.emergencyevent.dto.EmergencyEventRequest;
import com.pulse.emergencyevent.model.EmergencyEvent;
import com.pulse.emergencyevent.service.EmergencyEventService;
import com.pulse.medicalrecord.model.MedicalRecordEntry;
import com.pulse.medicalrecord.service.MedicalRecordEntryService;
import com.pulse.prescription.model.Prescription;
import com.pulse.user.model.Doctor;
import com.pulse.user.model.EmergencyWorker;
import com.pulse.user.model.Patient;
import com.pulse.user.repository.DoctorRepository;
import com.pulse.user.repository.EmergencyWorkerRepository;
import com.pulse.security.service.JwtService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/emergencyevents")
public class EmergencyEventController {

    private final EmergencyEventService eventService;

    private final DoctorRepository doctorRepo;
    private final JwtService jwtService;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public EmergencyEventController(EmergencyEventService eventService,
                                    DoctorRepository doctorRepo,
                                    JwtService jwtService) {
        this.eventService = eventService;
        this.doctorRepo = doctorRepo;
        this.jwtService = jwtService;
    }

    @PostMapping("/add")
    public ResponseEntity<EmergencyEvent> addEvent(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody EmergencyEventRequest dto
    ) {
        String token = authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : authHeader;
        UserDetails userDetails = jwtService.getUserFromToken(token);
        if (!jwtService.isTokenValid(token, userDetails)) {
            throw new RuntimeException("Invalid or expired JWT");
        }


        boolean isDoctor = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"));
        if (!isDoctor) {
            throw new AccessDeniedException("You are not authorized to add emergency events");
        }


        String username = userDetails.getUsername();
        Doctor doctor = doctorRepo.findByEmail(username);
        if (doctor == null) {
            throw new AccessDeniedException("You are not registered as a Doctor");
        }


        MedicalRecordEntry mre = new MedicalRecordEntry();
        mre.setTitle(dto.getMreTitle());
        mre.setTimestamp(LocalDateTime.now());
        com.pulse.user.model.Patient patient = new com.pulse.user.model.Patient();
        patient.setUserId(dto.getPatientId());
        mre.setPatient(patient);


        EmergencyEvent ev = new EmergencyEvent();
        ev.setNotes(dto.getNotes());
        ev.setDoctor(doctor);
        ev.setMedicalRecordEntry(mre);


        EmergencyEvent saved = eventService.createEventWithMre(ev, mre);

        logger.info("EmergencyEvent created id={} by doctor={}",
                saved.getEmergencyEventId(),
                doctor.getUserId());
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<EmergencyEvent>> listEvents(
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
        List<EmergencyEvent> list = eventService.findAllByPatientId(patientId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EmergencyEvent> getById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long eventId) {

        String token = authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : authHeader;

        UserDetails userDetails = jwtService.getUserFromToken(token);
        if (!jwtService.isTokenValid(token, userDetails)) {
            throw new RuntimeException("Invalid or expired JWT");
        }


        EmergencyEvent event = eventService.findById(eventId);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(event);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/patient/me")
    public ResponseEntity<List<EmergencyEvent>> listMyEvents(@RequestHeader("Authorization") String authHeader) {

        String token = authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : authHeader;

        UserDetails userDetails = jwtService.getUserFromToken(token);
        if (!jwtService.isTokenValid(token, userDetails)) {
            throw new RuntimeException("Invalid or expired JWT");
        }
        if (userDetails instanceof Patient patient)
        {
            List<EmergencyEvent> list = eventService.findAllByPatientId(patient.getUserId());
            return ResponseEntity.ok(list);
        }


        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}