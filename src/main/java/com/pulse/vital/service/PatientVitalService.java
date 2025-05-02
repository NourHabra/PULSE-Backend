package com.pulse.vital.service;


import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.pulse.vital.dto.PatientVitalDto;
import com.pulse.vital.model.PatientVital;
import com.pulse.vital.repository.PatientVitalRepository;
import com.pulse.vital.model.Vital;
import com.pulse.vital.repository.VitalRepository;
import com.pulse.user.model.Patient;
import com.pulse.user.repository.PatientRepository;

@Service
@Transactional
public class PatientVitalService {
    @Autowired
    private PatientVitalRepository repo;
    @Autowired
    private PatientRepository patientRepo;
    @Autowired
    private VitalRepository vitalRepo;

    public List<PatientVitalDto> getByPatientId(Long patientId) {
        return repo.findAll().stream()
                .filter(pv -> pv.getPatient().getUserId().equals(patientId))
                .map(pv -> new PatientVitalDto(
                        pv.getId(),
                        pv.getPatient().getUserId(),
                        pv.getVital().getVitalId(),
                        pv.getVital().getName(),
                        pv.getVital().getDescription(),
                        pv.getVital().getNormalValue(),
                        pv.getMeasurement(),
                        pv.getTimestamp()
                ))
                .collect(Collectors.toList());
    }

    public PatientVitalDto create(PatientVital pv) {
        Patient patient = patientRepo.findById(pv.getPatient().getUserId())
                .orElseThrow(() -> new RuntimeException("Patient not found: " + pv.getPatient().getUserId()));
        Vital vital = vitalRepo.findById(pv.getVital().getVitalId())
                .orElseThrow(() -> new RuntimeException("Vital not found: " + pv.getVital().getVitalId()));
        pv.setPatient(patient);
        pv.setVital(vital);
        PatientVital saved = repo.save(pv);
        return new PatientVitalDto(
                saved.getId(),
                patient.getUserId(),
                vital.getVitalId(),
                vital.getName(),
                vital.getDescription(),
                vital.getNormalValue(),
                saved.getMeasurement(),
                saved.getTimestamp()
        );
    }

    public PatientVitalDto update(Long id, PatientVital pv) {
        PatientVital existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("PatientVital not found: " + id));
        existing.setMeasurement(pv.getMeasurement());
        existing.setTimestamp(pv.getTimestamp());
        // initialize lazy fields
        Long pid = existing.getPatient().getUserId();
        Vital v = existing.getVital();
        String name = v.getName();
        String desc = v.getDescription();
        String norm = v.getNormalValue();
        PatientVital updated = repo.save(existing);
        return new PatientVitalDto(
                updated.getId(),
                pid,
                updated.getVital().getVitalId(),
                name,
                desc,
                norm,
                updated.getMeasurement(),
                updated.getTimestamp()
        );
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}