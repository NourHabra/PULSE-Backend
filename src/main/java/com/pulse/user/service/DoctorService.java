package com.pulse.user.service;

import com.pulse.user.dto.DoctorLoginDto;
import com.pulse.user.dto.DoctorRegisterDto;
import com.pulse.user.model.Doctor;
import com.pulse.user.repository.DoctorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.pulse.exception.EmailAlreadyExistsException;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    public DoctorService(DoctorRepository doctorRepository, PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Doctor register(DoctorRegisterDto dto) {
        if (doctorRepository.findByEmail(dto.getEmail()) != null) {
            throw new EmailAlreadyExistsException(dto.getEmail());
        }

        Doctor doctor = new Doctor();
        doctor.setFirstName(dto.getFirstName());
        doctor.setLastName(dto.getLastName());
        doctor.setEmail(dto.getEmail());
        doctor.setPassword(passwordEncoder.encode(dto.getPassword()));
        doctor.setRole("DOCTOR");

        doctor.setSpecialization(dto.getSpecialization());
        doctor.setLicenseNumber(dto.getLicenseNumber());
        doctor.setWorkingHours(dto.getWorkingHours());
        doctor.setBiography(dto.getBiography());
        doctor.setGender(dto.getGender());
        doctor.setDateOfBirth(dto.getDateOfBirth());
        doctor.setPlaceOfBirth(dto.getPlaceOfBirth());
        doctor.setMobileNumber(dto.getMobileNumber());
        doctor.setAddress(dto.getAddress());
        doctor.setPictureUrl(dto.getPictureUrl());
        return doctorRepository.save(doctor);
    }

    public Doctor login(DoctorLoginDto dto) {
        Doctor doctor = doctorRepository.findByEmail(dto.getEmail());
        if (doctor == null || !passwordEncoder.matches(dto.getPassword(), doctor.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return doctor;
    }
}
