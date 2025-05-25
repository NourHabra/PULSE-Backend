package com.pulse.diagnosis.service;


import com.pulse.diagnosis.model.Diagnosis;
import com.pulse.diagnosis.repository.DiagnosisRepository;
import com.pulse.emergencyevent.model.EmergencyEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import com.pulse.medicalrecord.model.MedicalRecordEntry;
import com.pulse.medicalrecord.repository.MedicalRecordEntryRepository;
import com.pulse.user.model.Doctor;
import com.pulse.user.repository.DoctorRepository;
import java.time.LocalDateTime;


@Service
public class DiagnosisService {
    private final DiagnosisRepository diagRepo;
    private final MedicalRecordEntryRepository mreRepo;

    public DiagnosisService(DiagnosisRepository diagRepo,
                            MedicalRecordEntryRepository mreRepo) {
        this.diagRepo = diagRepo;
        this.mreRepo = mreRepo;
    }

    @Transactional
    public Diagnosis createDiagnosisWithMre(Diagnosis diag, MedicalRecordEntry mre) {
        MedicalRecordEntry savedMre = mreRepo.save(mre);
        diag.setMedicalRecordEntry(savedMre);
        return diagRepo.saveAndFlush(diag);
    }

    public List<Diagnosis> findAllByPatientId(Long patientId) {
        return diagRepo.findByMedicalRecordEntry_Patient_UserId(patientId);
    }

    public Diagnosis findById(Long id) {
        return diagRepo.findById(id).orElse(null);
    }
}