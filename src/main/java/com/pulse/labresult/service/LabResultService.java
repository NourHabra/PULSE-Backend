package com.pulse.labresult.service;


import com.pulse.labresult.model.LabResult;
import com.pulse.labresult.repository.LabResultRepository;
import com.pulse.medicalrecord.repository.MedicalRecordEntryRepository;
import com.pulse.medicalrecord.model.MedicalRecordEntry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class LabResultService {
    private final LabResultRepository repo;
    private final MedicalRecordEntryRepository mreRepo;

    public LabResultService(LabResultRepository repo, MedicalRecordEntryRepository mreRepo) {
        this.repo = repo;
        this.mreRepo = mreRepo;
    }

    @Transactional
    public LabResult createWithMre(LabResult lr, MedicalRecordEntry mre) {
        MedicalRecordEntry savedMre = mreRepo.save(mre);
        lr.setMedicalRecordEntry(savedMre);
        return repo.saveAndFlush(lr);
    }


    public LabResult findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("LabResult not found"));
    }

    public List<LabResult> findAllByPatientId(Long patientId) {
        return repo.findByMedicalRecordEntry_Patient_UserId(patientId);
    }
}