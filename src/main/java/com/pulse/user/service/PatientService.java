package com.pulse.user.service;

import com.pulse.user.dto.PatientLoginDto;
import com.pulse.user.dto.PatientRegisterDto;
import com.pulse.user.dto.PatientSummaryDto;
import com.pulse.user.model.Patient;
import com.pulse.user.repository.PatientRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.pulse.exception.EmailAlreadyExistsException;
import com.pulse.user.dto.PatientUpdateDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;


    public PatientService(PatientRepository patientRepository, PasswordEncoder passwordEncoder) {
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;

    }

    public Patient register(PatientRegisterDto dto) {
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
//        patient.setFingerprint(dto.getFingerprint());
        patient.setGender(dto.getGender());
        patient.setMobileNumber(dto.getMobileNumber());
        patient.setDateOfBirth(dto.getDateOfBirth());
        patient.setPlaceOfBirth(dto.getPlaceOfBirth());
        patient.setAddress(dto.getAddress());
        patient.setPictureUrl(dto.getPictureUrl());
        patient.setIdImage(dto.getIdImage());
        Patient saved = patientRepository.save(patient);
//        fhirPatientService.pushToFhir(saved);
        return saved;
    }

    public Patient login(PatientLoginDto dto) {
        Patient patient = patientRepository.findByEmail(dto.getEmail());
        if (patient == null || !passwordEncoder.matches(dto.getPassword(), patient.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return patient;
    }


    public Patient updatePatient(Patient patient, PatientUpdateDto dto) {
        if (dto.getFirstName() != null) patient.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) patient.setLastName(dto.getLastName());
        if (dto.getGender() != null) patient.setGender(dto.getGender());
        if (dto.getDateOfBirth() != null) patient.setDateOfBirth(dto.getDateOfBirth());
        if (dto.getPlaceOfBirth() != null) patient.setPlaceOfBirth(dto.getPlaceOfBirth());
        if (dto.getMobileNumber() != null) patient.setMobileNumber(dto.getMobileNumber());
        if (dto.getAddress() != null) patient.setAddress(dto.getAddress());
        if (dto.getPictureUrl() != null) patient.setPictureUrl(dto.getPictureUrl());
        if (dto.getHeight() != null) patient.setHeight(dto.getHeight());
        if (dto.getWeight() != null) patient.setWeight(dto.getWeight());
        if (dto.getBloodType() != null) patient.setBloodType(dto.getBloodType());

        return patientRepository.save(patient);
    }

    public Patient getPatientById(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }
    public Patient getPatientByEmail(String email) {
        return patientRepository.findByEmail(email);
    }

    public List<PatientSummaryDto> getAllPatientSummaries() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream()
                .map(patient -> new PatientSummaryDto(patient.getUserId(),
                        patient.getFirstName() + " " + patient.getLastName(),
                        patient.getPictureUrl()))
                .collect(Collectors.toList());
    }
}
