package com.pulse.user.controller;

import com.pulse.security.service.JwtService;
import com.pulse.user.dto.LabTechnicianRegisterDto;
import com.pulse.user.dto.LabTechnicianLoginDto;
import com.pulse.user.dto.UserLoginResponse;
import com.pulse.user.model.LabTechnician;
import com.pulse.user.service.LabTechnicianService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class LabTechnicianController {

    private final LabTechnicianService labTechnicianService;
    private final JwtService jwtService;

    public LabTechnicianController(LabTechnicianService labTechnicianService, JwtService jwtService) {
        this.labTechnicianService = labTechnicianService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register/labtechnician")
    public ResponseEntity<UserLoginResponse> registerLabTechnician(@RequestBody LabTechnicianRegisterDto dto) {
        LabTechnician technician = labTechnicianService.register(dto);
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
