package com.pulse.prescription.repository;


import com.pulse.prescription.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByMedicalRecordEntry_Patient_UserId(Long patientId);
}