package com.pulse.prescription.service;


import com.pulse.medicalrecord.model.MedicalRecordEntry;
import com.pulse.medicalrecord.repository.MedicalRecordEntryRepository;
import com.pulse.prescription.model.Prescription;
import com.pulse.prescription.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

}