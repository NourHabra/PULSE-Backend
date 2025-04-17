package com.pulse.user.controller;

import com.pulse.security.service.JwtService;
import com.pulse.user.dto.EmergencyWorkerRegisterDto;
import com.pulse.user.dto.EmergencyWorkerLoginDto;
import com.pulse.user.dto.UserLoginResponse;
import com.pulse.user.model.EmergencyWorker;
import com.pulse.user.service.EmergencyWorkerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.pulse.email.service.ActivationService;

@RestController
@RequestMapping("/auth")
public class EmergencyWorkerController {

    private final EmergencyWorkerService emergencyWorkerService;
    private final JwtService jwtService;
    private final ActivationService activationSvc;

    public EmergencyWorkerController(EmergencyWorkerService emergencyWorkerService, JwtService jwtService, ActivationService activationSvc) {
        this.emergencyWorkerService = emergencyWorkerService;
        this.jwtService = jwtService;
        this.activationSvc = activationSvc;
    }

    @PostMapping("/register/emergencyworker")
    public ResponseEntity<UserLoginResponse> registerEmergencyWorker(@RequestBody EmergencyWorkerRegisterDto dto) {
        EmergencyWorker worker = emergencyWorkerService.register(dto);
        activationSvc.sendActivation(worker);
        String token = jwtService.generateToken(worker);

        return ResponseEntity.ok(new UserLoginResponse(
                "Emergency Worker registered successfully",
                token,
                jwtService.getExpirationTime(),
                worker
        ));
    }

    @PostMapping("/login/emergencyworker")
    public ResponseEntity<UserLoginResponse> loginEmergencyWorker(@RequestBody EmergencyWorkerLoginDto dto) {
        EmergencyWorker worker = emergencyWorkerService.login(dto);
        String token = jwtService.generateToken(worker);

        return ResponseEntity.ok(new UserLoginResponse(
                "Emergency Worker login successful",
                token,
                jwtService.getExpirationTime(),
                worker
        ));
    }
}
