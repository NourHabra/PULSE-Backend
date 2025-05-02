package com.pulse.user.service;

import com.pulse.exception.EmailAlreadyExistsException;
import com.pulse.user.dto.HealthEmployeeLoginDto;
import com.pulse.user.dto.HealthEmployeeRegisterDto;
import com.pulse.user.model.Admin;
import com.pulse.user.model.HealthEmployee;
import com.pulse.user.repository.AdminRepository;
import com.pulse.user.repository.HealthEmployeeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class HealthEmployeeService {

    private final HealthEmployeeRepository healthEmployeeRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public HealthEmployeeService(
            HealthEmployeeRepository healthEmployeeRepository,
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.healthEmployeeRepository = healthEmployeeRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

//    public HealthEmployee register(HealthEmployeeRegisterDto dto) {
//
//        if (healthEmployeeRepository.findByEmail(dto.getEmail()) != null) {
//            throw new EmailAlreadyExistsException(dto.getEmail());
//        }
//
//        Admin admin = adminRepository.findById(dto.getAuthorizedByAdminId())
//                .orElseThrow(() -> new RuntimeException("Authorizing admin not found"));
//
//
//        HealthEmployee employee = new HealthEmployee();
//        employee.setFirstName(dto.getFirstName());
//        employee.setLastName(dto.getLastName());
//        employee.setEmail(dto.getEmail());
//        employee.setPassword(passwordEncoder.encode(dto.getPassword()));
//        employee.setRole("HEALTH_EMPLOYEE");
//        employee.setAuthorizedBy(admin);
//
//        return healthEmployeeRepository.save(employee);
//    }

    public HealthEmployee addHealthEmployee(HealthEmployeeRegisterDto dto, Long adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        HealthEmployee healthEmployee = new HealthEmployee();
        healthEmployee.setFirstName(dto.getFirstName());
        healthEmployee.setLastName(dto.getLastName());
        healthEmployee.setEmail(dto.getEmail());
        healthEmployee.setPassword(passwordEncoder.encode(dto.getPassword()));
        healthEmployee.setAuthorizedBy(admin);
        healthEmployee.setRole("Health Employee");
        healthEmployee.setGender(dto.getGender());
        healthEmployee.setDateOfBirth(dto.getDateOfBirth());
        healthEmployee.setPlaceOfBirth(dto.getPlaceOfBirth());
        healthEmployee.setMobileNumber(dto.getMobileNumber());
        healthEmployee.setAddress(dto.getAddress());
        healthEmployee.setPictureUrl(dto.getPictureUrl());
        return healthEmployeeRepository.save(healthEmployee);
    }

    public HealthEmployee save(HealthEmployee healthEmployee) {
        return healthEmployeeRepository.save(healthEmployee);
    }
    public HealthEmployee login(HealthEmployeeLoginDto dto) {
        HealthEmployee employee = healthEmployeeRepository.findByEmail(dto.getEmail());
        if (employee == null || !passwordEncoder.matches(dto.getPassword(), employee.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return employee;
    }
}
