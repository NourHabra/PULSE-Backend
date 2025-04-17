package com.pulse.user.controller;

import com.pulse.security.service.JwtService;
import com.pulse.user.dto.PatientRegisterDto;
import com.pulse.user.dto.PatientLoginDto;
import com.pulse.user.dto.UserLoginResponse;
import com.pulse.user.model.Patient;
import com.pulse.user.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.pulse.email.service.ActivationService;

@RestController
@RequestMapping("/auth")
public class PatientController {

    private final PatientService patientService;
    private final JwtService jwtService;
    private final ActivationService activationSvc;

    public PatientController(PatientService patientService, JwtService jwtService,ActivationService activationSvc) {
        this.patientService = patientService;
        this.jwtService = jwtService;
        this.activationSvc = activationSvc;
    }

    @PostMapping("/register/patient")
    public ResponseEntity<UserLoginResponse> registerPatient(@RequestBody PatientRegisterDto dto) {
        Patient patient = patientService.register(dto);
        activationSvc.sendActivation(patient);
        String token = jwtService.generateToken(patient);

        return ResponseEntity.ok(new UserLoginResponse(
                "Patient registered successfully",
                token,
                jwtService.getExpirationTime(),
                patient
        ));
    }

    @PostMapping("/login/patient")
    public ResponseEntity<UserLoginResponse> loginPatient(@RequestBody PatientLoginDto dto) {
        Patient patient = patientService.login(dto);
        String token = jwtService.generateToken(patient);

        return ResponseEntity.ok(new UserLoginResponse(
                "Patient login successful",
                token,
                jwtService.getExpirationTime(),
                patient
        ));
    }
}
