package com.pulse.drug.service;


import com.pulse.drug.model.PrescriptionDrug;
import com.pulse.drug.repository.PrescriptionDrugRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionDrugService {
    private final PrescriptionDrugRepository repo;
    public PrescriptionDrugService(PrescriptionDrugRepository repo) { this.repo = repo; }

    public List<PrescriptionDrug> findByPatientId(Long patientId) {
        return repo.findByPrescription_MedicalRecordEntry_Patient_UserId(patientId);
    }

    public PrescriptionDrug create(PrescriptionDrug pd) {
        return repo.save(pd);
    }

    public PrescriptionDrug update(Long id, PrescriptionDrug pd) {
        PrescriptionDrug existing = repo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        existing.setDrug(pd.getDrug());
        existing.setDosage(pd.getDosage());
        existing.setDuration(pd.getDuration());
        existing.setNotes(pd.getNotes());
        return repo.save(existing);
    }


    public Optional<PrescriptionDrug> findById(Long id) {
        return repo.findById(id);
    }

    public PrescriptionDrug save(PrescriptionDrug pd) {
        return repo.save(pd);
    }

    public void delete(Long id) { repo.deleteById(id); }

    public List<PrescriptionDrug> findAllByMreId(Long mreId) {
        return repo.findByPrescription_MedicalRecordEntry_MedicalRecordEntryId(mreId);
    }

    public List<PrescriptionDrug> findByPrescriptionId(Long prescriptionId) {
        return repo.findByPrescription_PrescriptionId(prescriptionId);
    }

}