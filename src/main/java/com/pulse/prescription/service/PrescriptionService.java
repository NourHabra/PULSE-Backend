package com.pulse.prescription.service;


import com.pulse.labresult.model.LabResult;
import com.pulse.medicalrecord.model.MedicalRecordEntry;
import com.pulse.medicalrecord.repository.MedicalRecordEntryRepository;
import com.pulse.prescription.model.Prescription;
import com.pulse.prescription.repository.PrescriptionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescRepo;
    private final MedicalRecordEntryRepository mreRepo;

    public PrescriptionService(PrescriptionRepository prescRepo,
                               MedicalRecordEntryRepository mreRepo) {
        this.prescRepo = prescRepo;
        this.mreRepo = mreRepo;
    }

    @Transactional
    public Prescription createWithMre(Prescription presc, MedicalRecordEntry mre) {
        MedicalRecordEntry savedMre = mreRepo.save(mre);
        presc.setMedicalRecordEntry(savedMre);
        return prescRepo.saveAndFlush(presc);
    }

    public List<Prescription> findAllByPatientId(Long patientId) {
        return prescRepo.findByMedicalRecordEntry_Patient_UserId(patientId);
    }

    public Prescription findById(Long id) {
        return prescRepo.findById(id).orElse(null);
    }


    public Optional<Prescription> findByMreId(Long mreId) {
        return prescRepo.findByMedicalRecordEntry_MedicalRecordEntryId(mreId);
    }

    @Transactional
    public Prescription addToMre(Long mreId, Prescription presc) {
        MedicalRecordEntry mre = mreRepo.findById(mreId)
                .orElseThrow(() -> new EntityNotFoundException("MRE not found: " + mreId));

        presc.setMedicalRecordEntry(mre);
        return prescRepo.save(presc);
    }

}