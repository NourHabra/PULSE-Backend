package com.pulse.consent.controller;

import com.pulse.consent.model.Consent;
import com.pulse.consent.service.ConsentService;
import com.pulse.security.service.JwtService;
import com.pulse.notification.service.WebSocketPushService;
import com.pulse.user.dto.UserLoginResponse;
import com.pulse.user.model.Doctor;
import com.pulse.user.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/consents")
public class ConsentController {

    private static final Logger log = LoggerFactory.getLogger(ConsentController.class);

    private final ConsentService consentSvc;
    private final JwtService     jwtService;
    private final WebSocketPushService  pushSvc;

    public ConsentController(ConsentService consentSvc, JwtService jwtService,WebSocketPushService  pushSvc) {
        this.consentSvc = consentSvc;
        this.jwtService = jwtService;
        this.pushSvc    = pushSvc;
    }


    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping("/doctor/{doctorId}")
    public ResponseEntity<Consent> giveConsent(@PathVariable Long doctorId,
                                               @RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

            UserDetails patientDetails = jwtService.getUserFromToken(jwtToken);
            if (!jwtService.isTokenValid(jwtToken, patientDetails))
                throw new RuntimeException("Invalid or expired token");

            Long patientId = ((com.pulse.user.model.Patient) patientDetails).getUserId();

            Consent consent = consentSvc.giveConsent(patientId, doctorId);
            return ResponseEntity.status(HttpStatus.CREATED).body(consent);

        } catch (Exception e) {
            log.error("Consent creation failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }





    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping("/patient/{patientId}")
    public ResponseEntity<Consent> requestConsent(@PathVariable Long patientId,
                                                  @RequestHeader("Authorization") String token) {

        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        UserDetails doctorDetails = jwtService.getUserFromToken(jwtToken);
        if (!jwtService.isTokenValid(jwtToken, doctorDetails))
            throw new RuntimeException("Invalid or expired token");

        Long doctorId = ((com.pulse.user.model.Doctor) doctorDetails).getUserId();
        Consent c = consentSvc.requestConsent(patientId, doctorId);

        pushSvc.notifyPatientConsentPending(patientId, c.getId(), doctorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(c);
    }


    @PreAuthorize("hasRole('PATIENT')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> decide(@PathVariable Long id,
                                       @RequestParam("decision") String decision,
                                       @RequestHeader("Authorization") String token) {

        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        UserDetails patientDetails = jwtService.getUserFromToken(jwtToken);
        if (!jwtService.isTokenValid(jwtToken, patientDetails))
            throw new RuntimeException("Invalid or expired token");

        Long patientId = ((com.pulse.user.model.Patient) patientDetails).getUserId();

        boolean approve = "APPROVE".equalsIgnoreCase(decision);
        Consent c = consentSvc.decide(id, patientId, approve);

        pushSvc.notifyDoctorConsentResult(
                c.getDoctorId(),
                c.getId(),
                patientId,
                c.getStatus().name()
        );

        return ResponseEntity.ok().build();
    }
    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> getPatientsWithConsent(@RequestHeader("Authorization") String token) {
        String jwttoken = token.startsWith("Bearer ")
                ? token.substring(7)
                : token;
        UserDetails user = jwtService.getUserFromToken(jwttoken);


            Doctor doctor = (Doctor) user;


            List<Patient> patients = consentSvc.getPatientsByDoctorId(doctor.getUserId());
            return ResponseEntity.ok(patients);




    }
}
