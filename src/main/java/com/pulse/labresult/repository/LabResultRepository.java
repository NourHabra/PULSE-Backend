package com.pulse.labresult.repository;


import com.pulse.labresult.model.LabResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LabResultRepository extends JpaRepository<LabResult,Long> {
    List<LabResult> findByMedicalRecordEntry_Patient_UserId(Long patientId);
}
