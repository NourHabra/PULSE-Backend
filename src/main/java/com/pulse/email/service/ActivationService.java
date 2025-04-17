package com.pulse.email.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.stereotype.Service;
import com.pulse.email.service.EmailService;
import com.pulse.email.model.ActivationToken;
import com.pulse.user.model.User;
import com.pulse.email.repository.ActivationTokenRepository;
import com.pulse.user.repository.UserRepository;

@Service
public class ActivationService {

    private final ActivationTokenRepository tokenRepo;
    private final UserRepository userRepo;
    private final EmailService emailService;

    public ActivationService(ActivationTokenRepository tokenRepo,
                             UserRepository userRepo,
                             EmailService emailService) {
        this.tokenRepo = tokenRepo;
        this.userRepo = userRepo;
        this.emailService = emailService;
    }

    public void sendActivation(User user) {
        String token = UUID.randomUUID().toString();
        Instant expiry = Instant.now().plus(24, ChronoUnit.HOURS);
        tokenRepo.save(new ActivationToken(null, token, expiry, user));

        String baseUrl = "http://localhost:8080";
        String link = baseUrl + "/auth/activate?token=" + token;
        String body = String.format("Hello %s,\nPlease activate: %s", user.getFirstName(), link);
        emailService.sendSimpleMessage(user.getEmail(), "Activate your account", body);
    }

    public void verifyToken(String token) {
        ActivationToken at = tokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if (Instant.now().isAfter(at.getExpiry())) throw new RuntimeException("Token expired");
        User u = at.getUser();
        u.setEnabled(true);
        userRepo.save(u);
        tokenRepo.delete(at);
    }
}