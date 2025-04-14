package com.pulse.user.controller;

import com.pulse.security.service.JwtService;
import com.pulse.user.dto.DoctorRegisterDto;
import com.pulse.user.dto.DoctorLoginDto;
import com.pulse.user.dto.UserLoginResponse;
import com.pulse.user.model.Doctor;
import com.pulse.user.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class DoctorController {

    private final DoctorService doctorService;
    private final JwtService jwtService;

    public DoctorController(DoctorService doctorService, JwtService jwtService) {
        this.doctorService = doctorService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register/doctor")
    public ResponseEntity<UserLoginResponse> registerDoctor(@RequestBody DoctorRegisterDto dto) {
        Doctor doctor = doctorService.register(dto);
        String token = jwtService.generateToken(doctor);

        return ResponseEntity.ok(new UserLoginResponse(
                "Doctor registered successfully",
                token,
                jwtService.getExpirationTime(),
                doctor
        ));
    }

    @PostMapping("/login/doctor")
    public ResponseEntity<UserLoginResponse> loginDoctor(@RequestBody DoctorLoginDto dto) {
        Doctor doctor = doctorService.login(dto);
        String token = jwtService.generateToken(doctor);

        return ResponseEntity.ok(new UserLoginResponse(
                "Doctor login successful",
                token,
                jwtService.getExpirationTime(),
                doctor
        ));
    }
}
