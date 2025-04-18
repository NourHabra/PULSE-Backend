package com.pulse.user.controller;

import com.pulse.security.service.JwtService;
import com.pulse.user.dto.HealthEmployeeRegisterDto;
import com.pulse.user.dto.HealthEmployeeLoginDto;
import com.pulse.user.dto.UserLoginResponse;
import com.pulse.user.model.Admin;
import com.pulse.user.model.HealthEmployee;
import com.pulse.user.service.HealthEmployeeService;
import com.pulse.user.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.pulse.email.service.ActivationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/auth")
public class HealthEmployeeController {

    private final HealthEmployeeService healthEmployeeService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder; // Inject the PasswordEncoder
    private final ActivationService activationSvc;
    private final AdminService adminService;

    @Autowired
    public HealthEmployeeController(HealthEmployeeService healthEmployeeService,
                                    JwtService jwtService,
                                    PasswordEncoder passwordEncoder,
                                    ActivationService activationSvc
    ,AdminService adminService) {
        this.healthEmployeeService = healthEmployeeService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder; // Inject PasswordEncoder
        this.activationSvc = activationSvc;
        this.adminService = adminService;
    }


    @PostMapping("/add/healthEmployee")
    public ResponseEntity<UserLoginResponse> addHealthEmployee(@RequestBody HealthEmployeeRegisterDto dto, @RequestHeader("Authorization") String token) {
        try {

            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            Admin currentAdmin = (Admin) jwtService.getUserFromToken(jwtToken);

            if (currentAdmin == null || !"ADMIN".equals(currentAdmin.getRole())) {
                throw new RuntimeException("You are not authorized to add a health employee");
            }


            HealthEmployee healthEmployee = healthEmployeeService.addHealthEmployee(dto, currentAdmin.getUserId());
            activationSvc.sendActivation(healthEmployee);
            return ResponseEntity.ok(new UserLoginResponse(
                    "Health employee added successfully",
                    null,
                    null,
                    healthEmployee
            ));

        } catch (Exception e) {
            return ResponseEntity.status(403).body(new UserLoginResponse("Unauthorized", null, null, null));
        }
    }



    @PostMapping("/login/healthEmployee")
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
