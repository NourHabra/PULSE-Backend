package com.pulse.user.service;

import com.pulse.laboratory.model.Laboratory;
import com.pulse.laboratory.repository.LaboratoryRepository;
import com.pulse.user.dto.LabTechnicianLoginDto;
import com.pulse.user.dto.LabTechnicianRegisterDto;
import com.pulse.user.model.LabTechnician;
import com.pulse.user.repository.LabTechnicianRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.pulse.exception.EmailAlreadyExistsException;
@Service
public class LabTechnicianService {

    private final LabTechnicianRepository labTechnicianRepository;
    private final LaboratoryRepository laboratoryRepository;
    private final PasswordEncoder passwordEncoder;

    public LabTechnicianService(
            LabTechnicianRepository labTechnicianRepository,
            LaboratoryRepository laboratoryRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.labTechnicianRepository = labTechnicianRepository;
        this.laboratoryRepository = laboratoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LabTechnician register(LabTechnicianRegisterDto dto) {

        if (labTechnicianRepository.findByEmail(dto.getEmail()) != null) {
            throw new EmailAlreadyExistsException(dto.getEmail());
        }

        LabTechnician technician = new LabTechnician();
        technician.setFirstName(dto.getFirstName());
        technician.setLastName(dto.getLastName());
        technician.setEmail(dto.getEmail());
        technician.setPassword(passwordEncoder.encode(dto.getPassword()));
        technician.setRole("LAB_TECHNICIAN");

        technician.setLicenseNumber(dto.getLicenseNumber());



        technician.setGender(dto.getGender());
        technician.setDateOfBirth(dto.getDateOfBirth());
        technician.setPlaceOfBirth(dto.getPlaceOfBirth());
        technician.setMobileNumber(dto.getMobileNumber());
        technician.setAddress(dto.getAddress());
        technician.setPictureUrl(dto.getPictureUrl());
        return labTechnicianRepository.save(technician);
    }

    public LabTechnician login(LabTechnicianLoginDto dto) {
        LabTechnician technician = labTechnicianRepository.findByEmail(dto.getEmail());
        if (technician == null || !passwordEncoder.matches(dto.getPassword(), technician.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return technician;
    }
}
