package com.pulse.chronicdisease.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.pulse.chronicdisease.dto.PatientChronicDiseaseDto;
import com.pulse.chronicdisease.model.PatientChronicDisease;
import com.pulse.chronicdisease.repository.PatientChronicDiseaseRepository;
import com.pulse.chronicdisease.model.ChronicDisease;
import com.pulse.chronicdisease.repository.ChronicDiseaseRepository;
import com.pulse.user.model.Patient;
import com.pulse.user.repository.PatientRepository;

@Service
@Transactional
public class PatientChronicDiseaseService {
    @Autowired
    private PatientChronicDiseaseRepository repo;
    @Autowired
    private PatientRepository patientRepo;
    @Autowired
    private ChronicDiseaseRepository diseaseRepo;

    public List<PatientChronicDiseaseDto> getByPatientId(Long patientId) {
        return repo.findAll().stream()
                .filter(pcd -> pcd.getPatient().getUserId().equals(patientId))
                .map(pcd -> new PatientChronicDiseaseDto(
                        pcd.getId(),
                        pcd.getPatient().getUserId(),
                        pcd.getChronicDisease().getChronicDiseaseId(),
                        pcd.getChronicDisease().getDisease(),
                        pcd.getChronicDisease().getType(),
                        pcd.getIntensity(),
                        pcd.getStartDate()
                ))
                .collect(Collectors.toList());
    }

    public PatientChronicDiseaseDto create(PatientChronicDisease pcd) {
        Patient patient = patientRepo.findById(pcd.getPatient().getUserId())
                .orElseThrow(() -> new RuntimeException("Patient not found: " + pcd.getPatient().getUserId()));
        ChronicDisease disease = diseaseRepo.findById(pcd.getChronicDisease().getChronicDiseaseId())
                .orElseThrow(() -> new RuntimeException("ChronicDisease not found: " + pcd.getChronicDisease().getChronicDiseaseId()));
        pcd.setPatient(patient);
        pcd.setChronicDisease(disease);
        PatientChronicDisease saved = repo.save(pcd);
        return new PatientChronicDiseaseDto(
                saved.getId(),
                patient.getUserId(),
                disease.getChronicDiseaseId(),
                disease.getDisease(),
                disease.getType(),
                saved.getIntensity(),
                saved.getStartDate()
        );
    }

    public PatientChronicDiseaseDto update(Long id, PatientChronicDisease pcd) {
        PatientChronicDisease existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("PatientChronicDisease not found: " + id));
        existing.setIntensity(pcd.getIntensity());
        existing.setStartDate(pcd.getStartDate());
        // fetch details inside transaction
        Long pid = existing.getPatient().getUserId();
        ChronicDisease cd = existing.getChronicDisease();
        String diseaseName = cd.getDisease();
        String diseaseType = cd.getType();
        PatientChronicDisease updated = repo.save(existing);
        return new PatientChronicDiseaseDto(
                updated.getId(),
                pid,
                updated.getChronicDisease().getChronicDiseaseId(),
                diseaseName,
                diseaseType,
                updated.getIntensity(),
                updated.getStartDate()
        );
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}