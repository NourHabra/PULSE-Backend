package com.pulse.allergy.service;


import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.pulse.allergy.model.PatientAllergy;
import com.pulse.allergy.repository.PatientAllergyRepository;
import com.pulse.user.model.Patient;
import com.pulse.user.repository.PatientRepository;
import com.pulse.allergy.model.Allergy;
import com.pulse.allergy.repository.AllergyRepository;
import com.pulse.allergy.dto.PatientAllergyDto;
@Service
@Transactional
public class PatientAllergyService {

    @Autowired
    private PatientAllergyRepository repo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private AllergyRepository allergyRepo;

    public List<PatientAllergyDto> getByPatientId(Long patientId) {
        return repo.findAll().stream()
                .filter(pa -> pa.getPatient().getUserId().equals(patientId))
                .map(pa -> {
                    PatientAllergyDto dto = new PatientAllergyDto();
                    dto.setId(pa.getId());
                    dto.setPatientId(pa.getPatient().getUserId());
                    dto.setAllergyId(pa.getAllergy().getAllergyId());

                    dto.setAllergen(pa.getAllergy().getAllergen());
                    dto.setType(pa.getAllergy().getType());
                    dto.setIntensity(pa.getIntensity());
                    return dto;
                })
                .toList();
    }



    public PatientAllergy create(PatientAllergy pa) {
        Patient patient = patientRepo.findById(pa.getPatient().getUserId())
                .orElseThrow(() -> new RuntimeException("Patient not found: " + pa.getPatient().getUserId()));
        Allergy allergy = allergyRepo.findById(pa.getAllergy().getAllergyId())
                .orElseThrow(() -> new RuntimeException("Allergy not found: " + pa.getAllergy().getAllergyId()));
        pa.setPatient(patient);
        pa.setAllergy(allergy);
        return repo.save(pa);
    }

    public PatientAllergyDto update(Long id, PatientAllergy pa) {
        PatientAllergy existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("PatientAllergy not found: " + id));
        existing.setIntensity(pa.getIntensity());
        // initialize lazy fields while in transaction
        String allergen = existing.getAllergy().getAllergen();
        String type = existing.getAllergy().getType();
        Long pid = existing.getPatient().getUserId();
        PatientAllergy updated = repo.save(existing);
        return new PatientAllergyDto(
                updated.getId(),
                pid,
                updated.getAllergy().getAllergyId(),
                allergen,
                type,
                updated.getIntensity()
        );
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
