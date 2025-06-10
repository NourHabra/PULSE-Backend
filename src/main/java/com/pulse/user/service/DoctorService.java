package com.pulse.user.service;

import com.pulse.diagnosis.model.Diagnosis;
import com.pulse.diagnosis.repository.DiagnosisRepository;
import com.pulse.user.dto.*;
import com.pulse.user.model.Doctor;
import com.pulse.user.model.Patient;
import com.pulse.user.repository.DoctorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.pulse.exception.EmailAlreadyExistsException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;
@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final DiagnosisRepository diagnosisRepository;

    public DoctorService(DoctorRepository doctorRepository, PasswordEncoder passwordEncoder,DiagnosisRepository diagnosisRepository) {
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
        this.diagnosisRepository=diagnosisRepository;
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

        String coords = dto.getCoordinates().trim();
        if (!coords.startsWith("https://www.google.com/maps/place/")) {
            coords = "https://www.google.com/maps/place/" + coords;
        }
        doctor.setCoordinates(coords);
        return doctorRepository.save(doctor);
    }

    public Doctor login(DoctorLoginDto dto) {
        Doctor doctor = doctorRepository.findByEmail(dto.getEmail());
        if (doctor == null || !passwordEncoder.matches(dto.getPassword(), doctor.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return doctor;
    }

    @Cacheable(value = "featuredDoctors", key = "'featured'")
    public List<FeaturedDoctorDto> getTodayFeaturedDoctorsDto() {
        return doctorRepository.findTodayFeaturedDoctors()
                .stream()
                .map(FeaturedDoctorDto::fromEntity)
                .toList();
    }

    public DoctorProfileDto getDoctorProfile(Long id) {
        Doctor d = doctorRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Doctor id " + id));
        return DoctorProfileDto.fromEntity(d);
    }

    public List<DoctorProfileDto> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        return doctors.stream()
                .map(DoctorProfileDto::fromEntity)
                .collect(Collectors.toList());
    }


    public String convertToEmbedLink(String coordinatesLink) {
        if (coordinatesLink == null || !coordinatesLink.startsWith("https://www.google.com/maps/place/")) {
            throw new IllegalArgumentException("Invalid coordinates link format");
        }

        String coords = coordinatesLink.replace("https://www.google.com/maps/place/", "");

        return "https://www.google.com/maps?q=" + coords + "&z=15&output=embed";
    }
    public String getDoctorCoordinatesEmbedLink(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + id));
        String coordinatesLink = doctor.getCoordinates();

        return convertToEmbedLink(coordinatesLink);
    }

    public Doctor updateDoctor(Doctor doctor, DoctorUpdateDto dto) {
        if (dto.getFirstName() != null) doctor.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) doctor.setLastName(dto.getLastName());
        if (dto.getGender() != null) doctor.setGender(dto.getGender());
        if (dto.getSpecialization() != null) doctor.setSpecialization(dto.getSpecialization());
        if (dto.getLicenseNumber() != null) doctor.setLicenseNumber(dto.getLicenseNumber());
        if (dto.getWorkingHours() != null) doctor.setWorkingHours(dto.getWorkingHours());
        if (dto.getBiography() != null) doctor.setBiography(dto.getBiography());
        if (dto.getCoordinates() != null) doctor.setCoordinates(dto.getCoordinates());
        if (dto.getPictureUrl() != null) doctor.setPictureUrl(dto.getPictureUrl());
        if (dto.getAddress() != null) doctor.setAddress(dto.getAddress());
        if (dto.getMobileNumber() != null) doctor.setMobileNumber(dto.getMobileNumber());

        return doctorRepository.save(doctor);
    }


    public List<PatientSummaryDto> getLastDiagnosedPatients(Long doctorId, int limit) {
        Pageable page = PageRequest.of(0, limit * 3);
        List<Diagnosis> diag = diagnosisRepository
                .findByDoctor_UserIdOrderByMedicalRecordEntry_TimestampDesc(doctorId, page);

        List<PatientSummaryDto> result = new ArrayList<>();
        Set<Long> seen = new HashSet<>();

        for (Diagnosis d : diag) {
            Patient p = d.getMedicalRecordEntry().getPatient();
            if (seen.add(p.getUserId())) {
                result.add(new PatientSummaryDto(
                        p.getUserId(),
                        p.getFirstName(),
                        p.getLastName()
                ));
                if (result.size() == limit) break;
            }
        }
        return result;
    }

    private long countBetween(Long doctorId, LocalDateTime start, LocalDateTime end) {
        return diagnosisRepository
                .countByDoctor_UserIdAndMedicalRecordEntry_TimestampBetween(doctorId, start, end);
    }

    public long countDiagnosesToday(Long doctorId) {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end   = LocalDateTime.now();
        return countBetween(doctorId, start, end);
    }

    public long countDiagnosesThisWeek(Long doctorId) {
        LocalDate monday = LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDateTime start = monday.atStartOfDay();
        LocalDateTime end   = LocalDateTime.now();
        return countBetween(doctorId, start, end);
    }

    public long countDiagnosesThisMonth(Long doctorId) {
        LocalDate firstOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDateTime start = firstOfMonth.atStartOfDay();
        LocalDateTime end   = LocalDateTime.now();
        return countBetween(doctorId, start, end);
    }
}
