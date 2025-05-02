package com.pulse.emergencyevent.controller;


import com.pulse.emergencyevent.dto.EmergencyEventRequest;
import com.pulse.emergencyevent.model.EmergencyEvent;
import com.pulse.emergencyevent.service.EmergencyEventService;
import com.pulse.medicalrecord.model.MedicalRecordEntry;
import com.pulse.medicalrecord.service.MedicalRecordEntryService;
import com.pulse.user.model.EmergencyWorker;
import com.pulse.user.model.Patient;
import com.pulse.user.repository.EmergencyWorkerRepository;
import com.pulse.security.service.JwtService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/emergencyevents")
public class EmergencyEventController {

    private final EmergencyEventService eventService;

    private final EmergencyWorkerRepository workerRepo;
    private final JwtService jwtService;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public EmergencyEventController(EmergencyEventService eventService,
                                    EmergencyWorkerRepository workerRepo,
                                    JwtService jwtService) {
        this.eventService = eventService;
        this.workerRepo = workerRepo;
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


        boolean isWorker = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_EMERGENCY_WORKER"));
        if (!isWorker) {
            throw new AccessDeniedException("You are not an Emergency Worker");
        }


        String username = userDetails.getUsername();
        EmergencyWorker worker = workerRepo.findByEmail(username);
        if (worker == null) {
            throw new AccessDeniedException("You are not registered as an Emergency Worker");
        }


        MedicalRecordEntry mre = new MedicalRecordEntry();
        mre.setTitle(dto.getMreTitle());
        mre.setTimestamp(LocalDateTime.now());
        com.pulse.user.model.Patient patient = new com.pulse.user.model.Patient();
        patient.setUserId(dto.getPatientId());
        mre.setPatient(patient);


        EmergencyEvent ev = new EmergencyEvent();
        ev.setNotes(dto.getNotes());
        ev.setEmergencyWorker(worker);            // now 'worker' exists
        ev.setMedicalRecordEntry(mre);


        EmergencyEvent saved = eventService.createEventWithMre(ev, mre);

        logger.info("EmergencyEvent created id={} by worker={}",
                saved.getEmergencyEventId(),
                worker.getUserId());
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<EmergencyEvent>> listEvents(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long patientId
    ) {
        // validate token only
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
}