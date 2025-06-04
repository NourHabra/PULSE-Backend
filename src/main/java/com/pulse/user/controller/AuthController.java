package com.pulse.user.controller;

import com.pulse.security.service.JwtService;
import com.pulse.user.dto.*;
import com.pulse.user.model.Admin;
import com.pulse.user.model.User;
import com.pulse.user.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import com.pulse.email.service.ActivationService;
import org.springframework.security.access.prepost.PreAuthorize;
import com.pulse.user.service.UserService;
import com.pulse.email.service.OtpService;
import com.pulse.security.service.JwtService;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.AuthenticationException;

@RestController("userAuthController")
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final OtpService otpService;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager,
                          UserService userService,
                          OtpService otpService,
                          JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.otpService = otpService;
        this.jwtService = jwtService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {

            User user = userService.login(loginRequest);

            otpService.sendOtp(user.getEmail());

            return ResponseEntity.ok(Map.of(
                    "message", "OTP sent to your email. Please verify to complete login.",
                    "userId", user.getUserId()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }


    @PostMapping("/login/verify-otp")
    public ResponseEntity<UserLoginResponse> verifyOtp(@RequestBody OtpVerificationRequest otpRequest) {
        try {
            otpService.verifyOtp(otpRequest.getEmail(), otpRequest.getOtp());

            User user = userService.findByEmail(otpRequest.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            String token = jwtService.generateToken(user);

            return ResponseEntity.ok(new UserLoginResponse(
                    "Login successful! OTP verified.",
                    token,
                    jwtService.getExpirationTime(),
                    user
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new UserLoginResponse("OTP verification failed.", null, null, null)
            );
        }
    }


}
