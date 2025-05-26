package com.pulse.diagnosis.repository;


import com.pulse.diagnosis.model.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {
    List<Diagnosis> findByMedicalRecordEntry_Patient_UserId(Long patientId);

    Optional<Diagnosis> findByMedicalRecordEntry_MedicalRecordEntryId(Long mreId);
}