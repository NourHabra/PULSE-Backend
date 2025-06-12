package com.pulse.labresult.repository;


import com.pulse.labresult.model.LabResult;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LabResultRepository extends JpaRepository<LabResult,Long> {
    List<LabResult> findByMedicalRecordEntry_Patient_UserId(Long patientId);

    Optional<LabResult> findByMedicalRecordEntry_MedicalRecordEntryId(Long mreId);



    List<LabResult> findByTechnician_UserIdOrderByMedicalRecordEntry_TimestampDesc(
            Long labTechnicianUserId,
            Pageable pageable
    );

    long countByTechnician_UserIdAndMedicalRecordEntry_TimestampBetween(
            Long labTechnicianUserId,
            LocalDateTime start,
            LocalDateTime end
    );
}
