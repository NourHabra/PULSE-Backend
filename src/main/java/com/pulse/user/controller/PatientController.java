package com.pulse.user.controller;

import com.pulse.security.service.JwtService;
import com.pulse.user.dto.PatientRegisterDto;
import com.pulse.user.dto.PatientLoginDto;
import com.pulse.user.dto.UserLoginResponse;
import com.pulse.user.model.Patient;
import com.pulse.user.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.pulse.email.service.ActivationService;
import com.pulse.user.dto.PatientUpdateDto;
@RestController
@RequestMapping("/auth")
public class PatientController {

    private final PatientService patientService;
    private final JwtService jwtService;
    private final ActivationService activationSvc;

    public PatientController(PatientService patientService, JwtService jwtService, ActivationService activationSvc) {
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


    @GetMapping("/profile/patient")
    public ResponseEntity<UserLoginResponse> getPatientProfile(@RequestHeader("Authorization") String token) {

        String jwttoken = token.startsWith("Bearer ")
                ? token.substring(7)
                : token;
        UserDetails user = jwtService.getUserFromToken(jwttoken);

        if (user != null && user instanceof Patient) {
            Patient patient = (Patient) user;
            String tokenResponse = jwtService.generateToken(patient);


            return ResponseEntity.ok(new UserLoginResponse(
                    "Patient profile fetched successfully",
                    tokenResponse,
                    jwtService.getExpirationTime(),
                    patient
            ));
        } else {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new UserLoginResponse(
                    "Unauthorized access",
                    null,
                    null,
                    null
            ));
        }
    }




    @PutMapping("/update/patient")
    public ResponseEntity<UserLoginResponse> updatePatient(
            @RequestHeader("Authorization") String token,
            @RequestBody PatientUpdateDto dto
    ) {
        String jwttoken = token.startsWith("Bearer ")
                ? token.substring(7)
                : token;
        UserDetails user = jwtService.getUserFromToken(jwttoken);

        if (user != null && user instanceof Patient) {
            Patient patient = (Patient) user;

            Patient updated = patientService.updatePatient(patient, dto);
            String newToken = jwtService.generateToken(updated);

            return ResponseEntity.ok(new UserLoginResponse(
                    "Patient profile updated successfully",
                    newToken,
                    jwtService.getExpirationTime(),
                    updated
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new UserLoginResponse(
                    "Unauthorized access",
                    null,
                    null,
                    null
            ));
        }
    }

}



