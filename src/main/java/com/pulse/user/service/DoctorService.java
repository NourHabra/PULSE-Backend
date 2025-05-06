package com.pulse.user.service;

import com.pulse.user.dto.DoctorLoginDto;
import com.pulse.user.dto.DoctorProfileDto;
import com.pulse.user.dto.DoctorRegisterDto;
import com.pulse.user.dto.FeaturedDoctorDto;
import com.pulse.user.model.Doctor;
import com.pulse.user.repository.DoctorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.pulse.exception.EmailAlreadyExistsException;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

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
}
