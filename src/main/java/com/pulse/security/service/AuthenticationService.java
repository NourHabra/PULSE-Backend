//package com.pulse.security.service;
//
//import com.pulse.user.dto.AdminRegisterDto;
//import com.pulse.user.dto.AdminLoginDto;
//import com.pulse.user.model.Admin;
//import com.pulse.user.repository.AdminRepository;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//public class AuthenticationService {
//
//    private final AdminRepository adminRepository;
//    private final PasswordEncoder passwordEncoder;  // For encoding passwords
//
//    public AuthenticationService(
//            AdminRepository adminRepository,
//            PasswordEncoder passwordEncoder
//    ) {
//        this.adminRepository = adminRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    public Admin registerAdmin(AdminRegisterDto dto) {
//        Admin admin = new Admin();
//        admin.setFirstName(dto.getFirstName());
//        admin.setLastName(dto.getLastName());
//        admin.setEmail(dto.getEmail());
//        admin.setPassword(passwordEncoder.encode(dto.getPassword()));
//        admin.setRole("ADMIN"); // Optional — constructor sets this too
//
//        return adminRepository.save(admin);
//    }
//
//    public Admin loginAdmin(AdminLoginDto dto) {
//        // Find the admin by email
//        Admin admin = adminRepository.findByEmail(dto.getEmail());
//        if (admin == null) {
//            throw new RuntimeException("Admin not found with email: " + dto.getEmail());
//        }
//        // Check the password
//        boolean matches = passwordEncoder.matches(dto.getPassword(), admin.getPassword());
//        if (!matches) {
//            throw new RuntimeException("Invalid password");
//        }
//        // Optionally check admin code or any extra logic
//        return admin;
//    }
//}
