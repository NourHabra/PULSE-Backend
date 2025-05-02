package com.pulse.drug.controller;


import com.pulse.drug.dto.PrescriptionDrugRequest;
import com.pulse.drug.model.Drug;
import com.pulse.drug.repository.DrugRepository;
import com.pulse.prescription.repository.PrescriptionRepository;
import com.pulse.drug.model.PrescriptionDrug;
import com.pulse.drug.service.PrescriptionDrugService;
import com.pulse.prescription.model.Prescription;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/prescription-drug")
public class PrescriptionDrugController {
    private final PrescriptionDrugService service;
    private final PrescriptionRepository prescriptionRepo;
    private final DrugRepository drugRepo;

    public PrescriptionDrugController(PrescriptionDrugService service,PrescriptionRepository prescriptionRepo,DrugRepository drugRepo) {
        this.service = service;
        this.prescriptionRepo=prescriptionRepo;
        this.drugRepo=drugRepo;
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PrescriptionDrug>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(service.findByPatientId(patientId));
    }

    @PostMapping("/add")
    public ResponseEntity<PrescriptionDrug> addPrescriptionDrug(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody PrescriptionDrugRequest dto) {

        // … your JWT + role checks …

        // 1) fetch Prescription
        Prescription presc = prescriptionRepo.findById(dto.getPrescriptionId())
                .orElseThrow(() -> new RuntimeException("No Prescription id=" + dto.getPrescriptionId()));

        // 2) fetch Drug (this was missing)
        Drug drug = drugRepo.findById(dto.getDrugId())
                .orElseThrow(() -> new RuntimeException("No Drug id=" + dto.getDrugId()));

        // 3) build the join entity
        PrescriptionDrug pd = new PrescriptionDrug();
        pd.setPrescription(presc);
        pd.setDrug(drug);
        pd.setDuration(dto.getDuration());
        pd.setDosage(dto.getDosage());
        pd.setNotes(dto.getNotes());

        // 4) save
        PrescriptionDrug saved = service.create(pd);
        return ResponseEntity.ok(saved);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PrescriptionDrug> patchPrescriptionDrug(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        // 1) JWT validation (omitted for brevity)

        // 2) Load existing
        PrescriptionDrug existing = service.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No PrescriptionDrug with id=" + id));

        // 3) Apply only the keys you care about
        if (updates.containsKey("duration")) {
            existing.setDuration((String)updates.get("duration"));
        }
        if (updates.containsKey("dosage")) {
            existing.setDosage((String)updates.get("dosage"));
        }
        if (updates.containsKey("notes")) {
            existing.setNotes((String)updates.get("notes"));
        }

        // 4) Save and return
        PrescriptionDrug saved = service.save(existing);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
