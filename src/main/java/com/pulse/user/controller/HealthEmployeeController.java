package com.pulse.user.controller;

import com.pulse.security.service.JwtService;
import com.pulse.user.dto.HealthEmployeeRegisterDto;
import com.pulse.user.dto.HealthEmployeeLoginDto;
import com.pulse.user.dto.UserLoginResponse;
import com.pulse.user.model.HealthEmployee;
import com.pulse.user.service.HealthEmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.pulse.email.service.ActivationService;

@RestController
@RequestMapping("/auth")
public class HealthEmployeeController {

    private final HealthEmployeeService healthEmployeeService;
    private final JwtService jwtService;

    private final ActivationService activationSvc;
    public HealthEmployeeController(HealthEmployeeService healthEmployeeService, JwtService jwtService, ActivationService activationSvc) {
        this.healthEmployeeService = healthEmployeeService;
        this.jwtService = jwtService;
        this.activationSvc = activationSvc;
    }

    @PostMapping("/register/healthemployee")
    public ResponseEntity<UserLoginResponse> registerHealthEmployee(@RequestBody HealthEmployeeRegisterDto dto) {
        HealthEmployee employee = healthEmployeeService.register(dto);
        activationSvc.sendActivation(employee);
        String token = jwtService.generateToken(employee);

        return ResponseEntity.ok(new UserLoginResponse(
                "Health employee registered successfully",
                token,
                jwtService.getExpirationTime(),
                employee
        ));
    }

    @PostMapping("/login/healthemployee")
    public ResponseEntity<UserLoginResponse> loginHealthEmployee(@RequestBody HealthEmployeeLoginDto dto) {
        HealthEmployee employee = healthEmployeeService.login(dto);
        String token = jwtService.generateToken(employee);

        return ResponseEntity.ok(new UserLoginResponse(
                "Health employee login successful",
                token,
                jwtService.getExpirationTime(),
                employee
        ));
    }
}
