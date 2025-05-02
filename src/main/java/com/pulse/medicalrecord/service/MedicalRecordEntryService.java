package com.pulse.medicalrecord.service;



import com.pulse.medicalrecord.model.MedicalRecordEntry;
import com.pulse.medicalrecord.repository.MedicalRecordEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordEntryService {

    private final MedicalRecordEntryRepository repo;

    public MedicalRecordEntryService(MedicalRecordEntryRepository repo) {
        this.repo = repo;
    }

    /** used by controller to create the MRE **/
    public MedicalRecordEntry create(MedicalRecordEntry entry) {
        return repo.save(entry);
    }


    public List<MedicalRecordEntry> findAllByPatientId(Long patientId) {
        return repo.findByPatient_UserId(patientId);
    }

    public MedicalRecordEntry findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("MedicalRecordEntry not found"));
    }
}