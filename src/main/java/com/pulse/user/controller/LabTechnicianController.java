package com.pulse.user.controller;

import com.pulse.security.service.JwtService;
import com.pulse.user.dto.*;
import com.pulse.user.model.LabTechnician;
import com.pulse.user.service.LabTechnicianService;
import com.pulse.util.FileStorageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.pulse.email.service.ActivationService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class LabTechnicianController {

    private final LabTechnicianService labTechnicianService;
    private final JwtService jwtService;
    private final ActivationService activationSvc;

    public LabTechnicianController(LabTechnicianService labTechnicianService, JwtService jwtService,ActivationService activationSvc) {
        this.labTechnicianService = labTechnicianService;
        this.jwtService = jwtService;
        this.activationSvc = activationSvc;
    }

    @PostMapping("/register/labtechnician")
    public ResponseEntity<UserLoginResponse> registerLabTechnician(
            @RequestPart("data") LabTechnicianRegisterDto dto,
            @RequestPart("profilePicture") MultipartFile profilePictureFile
    ) throws IOException {
        String picturePath = FileStorageUtil.saveFile(profilePictureFile, "profile_pictures");
        dto.setPictureUrl(picturePath);
        LabTechnician technician = labTechnicianService.register(dto);
        activationSvc.sendActivation(technician);
        String token = jwtService.generateToken(technician);

        return ResponseEntity.ok(new UserLoginResponse(
                "Lab Technician registered successfully",
                token,
                jwtService.getExpirationTime(),
                technician
        ));
    }

    @PostMapping("/login/labtechnician")
    public ResponseEntity<UserLoginResponse> loginLabTechnician(@RequestBody LabTechnicianLoginDto dto) {
        LabTechnician technician = labTechnicianService.login(dto);
        String token = jwtService.generateToken(technician);

        return ResponseEntity.ok(new UserLoginResponse(
                "Lab Technician login successful",
                token,
                jwtService.getExpirationTime(),
                technician
        ));
    }

    @PutMapping(value = "/update/labTechnician", consumes = "multipart/form-data")
    public ResponseEntity<UserLoginResponse> updateLabTechnician(
            @RequestHeader("Authorization") String token,
            @RequestPart("dto") LabTechnicianUpdateDto dto,
            @RequestPart(value = "profile_picture", required = false) MultipartFile profilePictureFile
    ) throws IOException {
        String jwttoken = token.startsWith("Bearer ") ? token.substring(7) : token;
        UserDetails user = jwtService.getUserFromToken(jwttoken);

        if (user != null && user instanceof LabTechnician) {
            LabTechnician labTechnician = (LabTechnician) user;

            if (profilePictureFile != null && !profilePictureFile.isEmpty()) {
                String picturePath = FileStorageUtil.saveFile(profilePictureFile, "profile_pictures");
                dto.setPictureUrl(picturePath);
            }

            LabTechnician updated = labTechnicianService.updateLabTechnician(labTechnician, dto);
            String newToken = jwtService.generateToken(updated);

            return ResponseEntity.ok(new UserLoginResponse(
                    "Lab Technician profile updated successfully",
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

    @GetMapping("/profile/labTechnician")
    public ResponseEntity<UserLoginResponse> getLabTechnicianProfile(@RequestHeader("Authorization") String token) {

        String jwttoken = token.startsWith("Bearer ")
                ? token.substring(7)
                : token;
        UserDetails user = jwtService.getUserFromToken(jwttoken);

        if (user != null && user instanceof LabTechnician) {
            LabTechnician labTechnician = (LabTechnician) user;
            String tokenResponse = jwtService.generateToken(labTechnician);

            return ResponseEntity.ok(new UserLoginResponse(
                    "Lab Technician profile fetched successfully",
                    tokenResponse,
                    jwtService.getExpirationTime(),
                    labTechnician
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

    @GetMapping("/lab/patients/recent")
    public ResponseEntity<?> getRecentPatients(
            @RequestHeader("Authorization") String token
    ) {
        String jwt = token.startsWith("Bearer ")
                ? token.substring(7)
                : token;

        UserDetails user = jwtService.getUserFromToken(jwt);
        if (!(user instanceof LabTechnician)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long labTechnicianId = ((LabTechnician) user).getUserId();
        List<PatientSummaryDto> last5 = labTechnicianService
                .getLastLabResultsPatients(labTechnicianId, 5);

        if (last5.isEmpty()) {
            return ResponseEntity
                    .ok("No patients found for this lab technician yet.");
        }

        return ResponseEntity.ok(last5);
    }

    @GetMapping("/labResults/count/today")
    public ResponseEntity<Long> countToday(@RequestHeader("Authorization") String token) {
        Long labTechnicianId = extractLabTechnicianId(token);
        if (labTechnicianId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        long count = labTechnicianService.countLabResultsToday(labTechnicianId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/labResults/count/week")
    public ResponseEntity<Long> countWeek(@RequestHeader("Authorization") String token) {
        Long labTechnicianId = extractLabTechnicianId(token);
        if (labTechnicianId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        long count = labTechnicianService.countLabResultsThisWeek(labTechnicianId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/labResults/count/month")
    public ResponseEntity<Long> countMonth(@RequestHeader("Authorization") String token) {
        Long labTechnicianId = extractLabTechnicianId(token);
        if (labTechnicianId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        long count = labTechnicianService.countLabResultsThisMonth(labTechnicianId);
        return ResponseEntity.ok(count);
    }
    @GetMapping("/labResults/count/year")
    public ResponseEntity<Long> countYear(@RequestHeader("Authorization") String token) {
        Long labTechnicianId = extractLabTechnicianId(token);
        if (labTechnicianId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        long count = labTechnicianService.countLabResultsThisYear(labTechnicianId);
        return ResponseEntity.ok(count);
    }

    private Long extractLabTechnicianId(String bearerToken) {
        String jwt = bearerToken.startsWith("Bearer ")
                ? bearerToken.substring(7)
                : bearerToken;
        UserDetails user = jwtService.getUserFromToken(jwt);
        if (user instanceof LabTechnician) {
            return ((LabTechnician) user).getUserId();
        }
        return null;
    }

}
