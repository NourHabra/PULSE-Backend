package com.pulse.email.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.pulse.email.service.ActivationService;
import com.pulse.email.service.OtpService;
import org.springframework.web.servlet.ModelAndView;
import com.pulse.email.repository.OtpEntryRepository;
import com.pulse.user.repository.UserRepository;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final ActivationService activationSvc;
    private final OtpService otpSvc;
    private final UserRepository userRepo;


    public AuthController(ActivationService activationSvc, OtpService otpSvc, UserRepository userRepo) {
        this.activationSvc = activationSvc;
        this.otpSvc = otpSvc;
        this.userRepo = userRepo;
    }


    @GetMapping("/activate")
    public ModelAndView activate(@RequestParam String token) {
        activationSvc.verifyToken(token); // enable the user
        return new ModelAndView("activation-success");
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> forgotPasswordSendOtp(@RequestParam String email) {
        if (userRepo.findByEmail(email) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not registered");
        }
        otpSvc.sendOtp(email);
        return ResponseEntity.ok("OTP sent");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestParam String email,
                                            @RequestParam int otp) {
        otpSvc.verifyOtp(email, otp);
        return ResponseEntity.ok("OTP verified");
    }

    @PostMapping("reset")
    public ResponseEntity<String> resetPassword(@RequestParam String email,
                                                @RequestParam int otp,
                                                @RequestParam String newPassword) {
        otpSvc.verifyOtp(email, otp);
        otpSvc.resetUserPassword(email, newPassword);
        return ResponseEntity.ok("Password reset successful");
    }



}