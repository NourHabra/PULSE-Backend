package com.pulse.user.controller;

import com.pulse.security.service.JwtService;
import com.pulse.user.dto.PharmacistRegisterDto;
import com.pulse.user.dto.PharmacistLoginDto;
import com.pulse.user.dto.UserLoginResponse;
import com.pulse.user.model.Pharmacist;
import com.pulse.user.service.PharmacistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.pulse.email.service.ActivationService;

@RestController
@RequestMapping("/auth")
public class PharmacistController {

    private final PharmacistService pharmacistService;
    private final JwtService jwtService;
    private final ActivationService activationSvc;

    public PharmacistController(PharmacistService pharmacistService, JwtService jwtService,ActivationService activationSvc) {
        this.pharmacistService = pharmacistService;
        this.jwtService = jwtService;
        this.activationSvc = activationSvc;
    }

    @PostMapping("/register/pharmacist")
    public ResponseEntity<UserLoginResponse> registerPharmacist(@RequestBody PharmacistRegisterDto dto) {
        Pharmacist pharmacist = pharmacistService.register(dto);
        activationSvc.sendActivation(pharmacist);
        String token = jwtService.generateToken(pharmacist);

        return ResponseEntity.ok(new UserLoginResponse(
                "Pharmacist registered successfully",
                token,
                jwtService.getExpirationTime(),
                pharmacist
        ));
    }

    @PostMapping("/login/pharmacist")
    public ResponseEntity<UserLoginResponse> loginPharmacist(@RequestBody PharmacistLoginDto dto) {
        Pharmacist pharmacist = pharmacistService.login(dto);
        String token = jwtService.generateToken(pharmacist);

        return ResponseEntity.ok(new UserLoginResponse(
                "Pharmacist login successful",
                token,
                jwtService.getExpirationTime(),
                pharmacist
        ));
    }
}
