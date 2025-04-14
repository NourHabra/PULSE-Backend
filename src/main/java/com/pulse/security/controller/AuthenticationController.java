//package com.pulse.security.controller;
//
//import com.pulse.security.service.AuthenticationService;
//import com.pulse.security.service.JwtService;
//import com.pulse.user.dto.AdminRegisterDto;
//import com.pulse.user.dto.AdminLoginDto;
//import com.pulse.user.dto.UserLoginResponse;
//import com.pulse.user.model.Admin;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthenticationController {
//
//    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
//
//    private final JwtService jwtService;
//    private final AuthenticationService authenticationService;
//
//    public AuthenticationController(
//            JwtService jwtService,
//            AuthenticationService authenticationService
//    ) {
//        this.jwtService = jwtService;
//        this.authenticationService = authenticationService;
//    }
//
//    // -------------------- REGISTER ADMIN --------------------
//    @PostMapping("/register/admin")
//    public ResponseEntity<UserLoginResponse> registerAdmin(
//            @RequestBody AdminRegisterDto adminDto
//    ) {
//        Admin admin = authenticationService.registerAdmin(adminDto);
//        String jwtToken = jwtService.generateToken(admin);
//
//        return ResponseEntity.ok(new UserLoginResponse(
//                "Admin registered successfully",
//                jwtToken,
//                jwtService.getExpirationTime(),
//                admin
//        ));
//    }
//
//    // -------------------- LOGIN ADMIN --------------------
//    @PostMapping("/login/admin")
//    public ResponseEntity<UserLoginResponse> loginAdmin(
//            @RequestBody AdminLoginDto adminLoginDto
//    ) {
//        log.info("POST /auth/login/admin -> {}", adminLoginDto.getEmail());
//
//        Admin admin = authenticationService.loginAdmin(adminLoginDto);
//        String jwtToken = jwtService.generateToken(admin);
//
//        log.info("Admin login successful -> {}", admin.getEmail());
//
//        return ResponseEntity.ok(new UserLoginResponse(
//                "Admin login successful",
//                jwtToken,
//                jwtService.getExpirationTime(),
//                admin
//        ));
//    }
//}
