package com.pulse.user.controller;

import com.pulse.security.service.JwtService;
import com.pulse.user.dto.AdminRegisterDto;
import com.pulse.user.dto.AdminLoginDto;
import com.pulse.user.dto.UserLoginResponse;
import com.pulse.user.model.Admin;
import com.pulse.user.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.pulse.email.service.ActivationService;
import org.springframework.security.access.prepost.PreAuthorize;
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

//    Removed for admin register control
//    @PostMapping("/register/admin")
//    public ResponseEntity<UserLoginResponse> registerAdmin(@RequestBody AdminRegisterDto dto) {
//        try {
//            Admin admin = adminService.register(dto);
//            activationSvc.sendActivation(admin);
//            String token = jwtService.generateToken(admin);
//            log.info(" admin register {}", admin);
//            log.info(" token {}", token);
//            return ResponseEntity.ok(new UserLoginResponse(
//                    "Admin registered successfully",
//                    token,
//                    jwtService.getExpirationTime(),
//                    admin
//            ));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(new UserLoginResponse(e.getMessage(), null, jwtService.getExpirationTime(), null));
//        }
//    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add/admin")
    public ResponseEntity<UserLoginResponse> addAdmin(@RequestBody AdminRegisterDto dto, @RequestHeader("Authorization") String token) {
        try {
            // Strip out 'Bearer ' if present
            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

            // Get user details from the JWT token
            UserDetails currentAdminDetails = jwtService.getUserFromToken(jwtToken);

            // Validate the token
            if (!jwtService.isTokenValid(jwtToken, currentAdminDetails)) {
                throw new RuntimeException("Invalid or expired token");
            }

            // Ensure the user has the ADMIN role
            boolean hasAdminRole = currentAdminDetails.getAuthorities()
                    .stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

            if (!hasAdminRole) {
                throw new RuntimeException("You are not authorized to add an admin");
            }

            // Proceed with adding the new admin
            Admin admin = adminService.register(dto);
            activationSvc.sendActivation(admin); // Send activation email
            String newAdminToken = jwtService.generateToken(admin);

            return ResponseEntity.ok(new UserLoginResponse(
                    "New admin created successfully",
                    newAdminToken,
                    jwtService.getExpirationTime(),
                    admin
            ));
        } catch (Exception e) {
            log.error("Error adding new admin: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new UserLoginResponse("Unauthorized", null, null, null));
        }
    }


    @PostMapping("/login/admin")
    public ResponseEntity<UserLoginResponse> loginAdmin(@RequestBody AdminLoginDto dto) {
        try {
            Admin admin = adminService.login(dto); // Use login method
            String token = jwtService.generateToken(admin); // Generate JWT token
            return ResponseEntity.ok(new UserLoginResponse(
                    "Admin login successful",
                    token,
                    jwtService.getExpirationTime(),
                    admin // You can include the admin details or remove it if needed
            ));
        } catch (Exception e) {
            return ResponseEntity.status(403).body(new UserLoginResponse(e.getMessage(), null, jwtService.getExpirationTime(), null));
        }
    }


}
