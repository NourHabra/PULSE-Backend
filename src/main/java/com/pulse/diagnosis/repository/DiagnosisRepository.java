package com.pulse.diagnosis.repository;

import java.time.LocalDateTime;
import com.pulse.diagnosis.model.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {
    List<Diagnosis> findByMedicalRecordEntry_Patient_UserId(Long patientId);

    Optional<Diagnosis> findByMedicalRecordEntry_MedicalRecordEntryId(Long mreId);
    List<Diagnosis> findByDoctor_UserIdOrderByMedicalRecordEntry_TimestampDesc(
            Long doctorUserId,
            Pageable pageable
    );

    long countByDoctor_UserIdAndMedicalRecordEntry_TimestampBetween(
            Long doctorUserId,
            LocalDateTime start,
            LocalDateTime end
    );
}