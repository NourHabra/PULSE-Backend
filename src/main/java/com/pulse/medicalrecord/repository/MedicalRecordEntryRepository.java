package com.pulse.medicalrecord.repository;

import com.pulse.medicalrecord.model.MedicalRecordEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalRecordEntryRepository extends JpaRepository<MedicalRecordEntry, Long> {
    List<MedicalRecordEntry> findByPatient_UserId(Long userId);
}