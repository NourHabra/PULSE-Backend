package com.pulse.user.controller;

import com.pulse.security.service.JwtService;
import com.pulse.user.dto.AdminRegisterDto;
import com.pulse.user.dto.AdminLoginDto;
import com.pulse.user.dto.UserLoginResponse;
import com.pulse.user.model.Admin;
import com.pulse.user.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.pulse.email.service.ActivationService;
@RestController
@RequestMapping("/auth")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);
    private final AdminService adminService;
    private final JwtService jwtService;
    private final ActivationService activationSvc;

    public AdminController(AdminService adminService, JwtService jwtService, ActivationService activationSvc) {
        this.adminService = adminService;
        this.jwtService = jwtService;
        this.activationSvc = activationSvc;
    }

    @PostMapping("/register/admin")
    public ResponseEntity<UserLoginResponse> registerAdmin(@RequestBody AdminRegisterDto dto) {
        try {
            Admin admin = adminService.register(dto);
            activationSvc.sendActivation(admin);
            String token = jwtService.generateToken(admin);
            log.info(" admin register {}", admin);
            log.info(" token {}", token);
            return ResponseEntity.ok(new UserLoginResponse(
                    "Admin registered successfully",
                    token,
                    jwtService.getExpirationTime(),
                    admin
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new UserLoginResponse(e.getMessage(), null, jwtService.getExpirationTime(), null));
        }
    }

    @PostMapping("/login/admin")
    public ResponseEntity<UserLoginResponse> loginAdmin(@RequestBody AdminLoginDto dto) {
        Admin admin = adminService.login(dto);
        String token = jwtService.generateToken(admin);

        return ResponseEntity.ok(new UserLoginResponse(
                "Admin login successful",
                token,
                jwtService.getExpirationTime(),
                admin
        ));
    }
}
