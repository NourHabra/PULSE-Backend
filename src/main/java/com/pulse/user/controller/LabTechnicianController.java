package com.pulse.user.controller;

import com.pulse.security.service.JwtService;
import com.pulse.user.dto.DoctorRegisterDto;
import com.pulse.user.dto.LabTechnicianRegisterDto;
import com.pulse.user.dto.LabTechnicianLoginDto;
import com.pulse.user.dto.UserLoginResponse;
import com.pulse.user.model.LabTechnician;
import com.pulse.user.service.LabTechnicianService;
import com.pulse.util.FileStorageUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.pulse.email.service.ActivationService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
}
