package com.pulse.user.service;

import com.pulse.user.dto.PatientLoginDto;
import com.pulse.user.dto.PatientRegisterDto;
import com.pulse.user.model.Patient;
import com.pulse.user.repository.PatientRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.pulse.exception.EmailAlreadyExistsException;
@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    public PatientService(PatientRepository patientRepository, PasswordEncoder passwordEncoder) {
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Patient register(PatientRegisterDto dto) {
        // ✅ Fix 403: Add email duplication check to avoid serialization failure
        if (patientRepository.findByEmail(dto.getEmail()) != null) {
            throw new EmailAlreadyExistsException(dto.getEmail());
        }

        Patient patient = new Patient();
        patient.setFirstName(dto.getFirstName());
        patient.setLastName(dto.getLastName());
        patient.setEmail(dto.getEmail());
        patient.setPassword(passwordEncoder.encode(dto.getPassword()));
        patient.setRole("PATIENT");

        patient.setHeight(dto.getHeight());
        patient.setWeight(dto.getWeight());
        patient.setBloodType(dto.getBloodType());

        // ✅ Make sure this is Base64 encoded before setting it
        patient.setFingerprint(dto.getFingerprint());

        return patientRepository.save(patient);
    }

    public Patient login(PatientLoginDto dto) {
        Patient patient = patientRepository.findByEmail(dto.getEmail());
        if (patient == null || !passwordEncoder.matches(dto.getPassword(), patient.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return patient;
    }
}
