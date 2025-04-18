package com.pulse.user.service;

import com.pulse.exception.EmailAlreadyExistsException;
import com.pulse.exception.WrongPasswordException;
import com.pulse.user.dto.AdminRegisterDto;
import com.pulse.user.dto.AdminLoginDto;
import com.pulse.user.model.Admin;
import com.pulse.user.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Admin register(AdminRegisterDto dto) {
        if (adminRepository.findByEmail(dto.getEmail()) != null) {
            throw new EmailAlreadyExistsException(dto.getEmail());
        }

        Admin admin = new Admin();
        admin.setFirstName(dto.getFirstName());
        admin.setLastName(dto.getLastName());
        admin.setEmail(dto.getEmail());
        admin.setPassword(passwordEncoder.encode(dto.getPassword()));
        admin.setRole("ADMIN");

        return adminRepository.save(admin);
    }
    public Admin login(AdminLoginDto dto) {
        Admin admin = adminRepository.findByEmail(dto.getEmail());
        if (admin == null || !passwordEncoder.matches(dto.getPassword(), admin.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return admin;
    }

    public Admin findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }
}
