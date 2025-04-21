package com.pulse.email.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.transaction.annotation.Transactional;
import com.pulse.user.repository.UserRepository;
import com.pulse.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.pulse.email.service.EmailService;
import com.pulse.email.model.OtpEntry;
import com.pulse.email.repository.OtpEntryRepository;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class OtpService {

    private final OtpEntryRepository otpRepo;
    private final EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final TemplateEngine templateEngine;

    public OtpService(OtpEntryRepository otpRepo, EmailService emailService,TemplateEngine templateEngine) {
        this.otpRepo = otpRepo;
        this.emailService = emailService;
        this.templateEngine=templateEngine;
    }
    private String generateOtpEmailHtml(int otp) {
        Context context = new Context();
        context.setVariable("otp", otp);


        return templateEngine.process("otp-email.html", context);
    }

    public void sendOtp(String email) {

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("This email is not registered.");
        }
        int code = ThreadLocalRandom.current().nextInt(100000, 1000000);
        Instant expiry = Instant.now().plus(10, ChronoUnit.MINUTES);
        otpRepo.save(new OtpEntry(null, email, code, expiry));

//        String text = String.format("Your OTP is: %d (expires in 10 minutes)", code);
        String htmlBody = generateOtpEmailHtml(code);

        emailService.sendSimpleMessage(email, "Your OTP code", htmlBody);
    }

    @Transactional
    public void verifyOtp(String email, int otp) {
        OtpEntry entry = otpRepo.findTopByEmailOrderByExpiryDesc(email)
                .orElseThrow(() -> new RuntimeException("Invalid OTP"));

        if (Instant.now().isAfter(entry.getExpiry())) {
            throw new RuntimeException("OTP expired");
        }

        if (entry.getCode() != otp) {
            throw new RuntimeException("Wrong OTP");
        }

        otpRepo.deleteByEmail(email);
    }
    public void resetUserPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email);
        if (user == null) throw new RuntimeException("User not found");


        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }


}