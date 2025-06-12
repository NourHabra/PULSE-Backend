package com.pulse.user.controller;

import com.pulse.security.service.JwtService;
import com.pulse.user.dto.*;
import com.pulse.user.model.Patient;
import com.pulse.user.service.PatientService;
import com.pulse.util.FileStorageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.pulse.email.service.ActivationService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
    public ResponseEntity<UserLoginResponse> registerPatient(
            @RequestPart("data") PatientRegisterDto dto,
            @RequestPart("picture") MultipartFile pictureFile,
             @RequestPart("idImage") MultipartFile idImageFile
    ) throws IOException {
        String picturePath = FileStorageUtil.saveFile(pictureFile, "profile_pictures");
        dto.setPictureUrl(picturePath);

        String idImagePath = FileStorageUtil.saveFile(idImageFile, "id_images");
        dto.setIdImage(idImagePath);

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




    @PutMapping(value = "/update/patient", consumes = "multipart/form-data")
    public ResponseEntity<UserLoginResponse> updatePatient(
            @RequestHeader("Authorization") String token,
            @RequestPart("dto") PatientUpdateDto dto,
            @RequestPart(value = "profile_picture", required = false) MultipartFile profilePictureFile
    ) throws IOException {
        String jwttoken = token.startsWith("Bearer ") ? token.substring(7) : token;
        UserDetails user = jwtService.getUserFromToken(jwttoken);

        if (user != null && user instanceof Patient) {
            Patient patient = (Patient) user;

            if (profilePictureFile != null && !profilePictureFile.isEmpty()) {
                String picturePath = FileStorageUtil.saveFile(profilePictureFile, "profile_pictures");
                dto.setPictureUrl(picturePath);
            }

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


    @GetMapping("/patients")
    public ResponseEntity<List<PatientSummaryDto>> getAllPatients() {
        List<PatientSummaryDto> patientSummaries = patientService.getAllPatientSummaries();
        return ResponseEntity.ok(patientSummaries);
    }

}



