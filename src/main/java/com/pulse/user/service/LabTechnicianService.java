package com.pulse.user.service;

import com.pulse.laboratory.model.Laboratory;
import com.pulse.laboratory.repository.LaboratoryRepository;
import com.pulse.labresult.model.LabResult;
import com.pulse.user.dto.LabTechnicianLoginDto;
import com.pulse.user.dto.LabTechnicianRegisterDto;
import com.pulse.user.dto.LabTechnicianUpdateDto;
import com.pulse.user.dto.PatientSummaryDto;
import com.pulse.user.model.LabTechnician;
import com.pulse.user.model.Patient;
import com.pulse.user.repository.LabTechnicianRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.pulse.exception.EmailAlreadyExistsException;
import com.pulse.labresult.repository.LabResultRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class LabTechnicianService {

    private final LabTechnicianRepository labTechnicianRepository;
    private final LaboratoryRepository laboratoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final LabResultRepository labResultRepository;

    public LabTechnicianService(
            LabTechnicianRepository labTechnicianRepository,
            LaboratoryRepository laboratoryRepository,
            PasswordEncoder passwordEncoder,
            LabResultRepository labResultRepository
    ) {
        this.labTechnicianRepository = labTechnicianRepository;
        this.laboratoryRepository = laboratoryRepository;
        this.passwordEncoder = passwordEncoder;
        this.labResultRepository=labResultRepository;
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

    @Transactional
    public LabTechnician updateLabTechnician(LabTechnician labTechnician, LabTechnicianUpdateDto dto) {
        if (dto.getFirstName() != null) {
            labTechnician.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            labTechnician.setLastName(dto.getLastName());
        }
        if (dto.getGender() != null) {
            labTechnician.setGender(dto.getGender());
        }
        if (dto.getAddress() != null) {
            labTechnician.setAddress(dto.getAddress());
        }
        if (dto.getMobileNumber() != null) {
            labTechnician.setMobileNumber(dto.getMobileNumber());
        }
        if (dto.getPictureUrl() != null) {

            labTechnician.setPictureUrl(dto.getPictureUrl());
        }
        if (dto.getLicenseNumber() != null) {
            labTechnician.setLicenseNumber(dto.getLicenseNumber());
        }
        if (dto.getTechnicianRole() != null) {
            labTechnician.setTechnicianRole(dto.getTechnicianRole());
        }
        if (dto.getWorkingLabId() != null) {
            Laboratory workingLab = laboratoryRepository.findById(dto.getWorkingLabId()).orElse(null);
            labTechnician.setWorkingLab(workingLab);
        }

        return labTechnicianRepository.save(labTechnician);
    }

    public List<PatientSummaryDto> getLastLabResultsPatients(Long labTechnicianId, int limit) {
        Pageable page = PageRequest.of(0, limit * 3);
        List<LabResult> labResults = labResultRepository
                .findByTechnician_UserIdOrderByMedicalRecordEntry_TimestampDesc(labTechnicianId, page);

        List<PatientSummaryDto> result = new ArrayList<>();
        Set<Long> seen = new HashSet<>();

        for (LabResult labResult : labResults) {
            Patient patient = labResult.getMedicalRecordEntry().getPatient();

            if (seen.add(patient.getUserId())) {
                result.add(new PatientSummaryDto(
                        patient.getUserId(),
                        patient.getFirstName() + " " + patient.getLastName(),
                        patient.getPictureUrl()
                ));
                if (result.size() == limit) break;
            }
        }

        return result;
    }


    private long countBetween(Long labTechnicianId, LocalDateTime start, LocalDateTime end) {
        return labResultRepository
                .countByTechnician_UserIdAndMedicalRecordEntry_TimestampBetween(labTechnicianId, start, end);
    }
    public long countLabResultsToday(Long labTechnicianId) {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDateTime.now();
        return countBetween(labTechnicianId, start, end);
    }
    public long countLabResultsThisWeek(Long labTechnicianId) {
        LocalDate monday = LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDateTime start = monday.atStartOfDay();
        LocalDateTime end = LocalDateTime.now();
        return countBetween(labTechnicianId, start, end);
    }
    public long countLabResultsThisMonth(Long labTechnicianId) {
        LocalDate firstOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDateTime start = firstOfMonth.atStartOfDay();
        LocalDateTime end = LocalDateTime.now();
        return countBetween(labTechnicianId, start, end);
    }




}
