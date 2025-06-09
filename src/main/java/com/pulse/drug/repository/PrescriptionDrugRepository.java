package com.pulse.drug.repository;

import com.pulse.drug.model.PrescriptionDrug;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface PrescriptionDrugRepository extends JpaRepository<PrescriptionDrug, Long> {
    List<PrescriptionDrug> findByPrescription_MedicalRecordEntry_Patient_UserId(Long patientId);

    List<PrescriptionDrug> findByPrescription_MedicalRecordEntry_MedicalRecordEntryId(Long mreId);

    List<PrescriptionDrug> findByPrescription_PrescriptionId(Long prescriptionId);
}