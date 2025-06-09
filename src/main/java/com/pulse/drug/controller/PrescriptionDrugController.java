package com.pulse.drug.controller;


import com.pulse.diagnosis.model.Diagnosis;
import com.pulse.drug.dto.PrescriptionDrugRequest;
import com.pulse.drug.model.Drug;
import com.pulse.drug.repository.DrugRepository;
import com.pulse.prescription.repository.PrescriptionRepository;
import com.pulse.drug.model.PrescriptionDrug;
import com.pulse.drug.service.PrescriptionDrugService;
import com.pulse.prescription.model.Prescription;
import com.pulse.security.service.JwtService;
import com.pulse.user.model.Patient;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final JwtService jwtService;

    public PrescriptionDrugController(PrescriptionDrugService service,PrescriptionRepository prescriptionRepo,DrugRepository drugRepo,JwtService jwtService) {
        this.service = service;
        this.prescriptionRepo=prescriptionRepo;
        this.drugRepo=drugRepo;
        this.jwtService=jwtService;
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PrescriptionDrug>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(service.findByPatientId(patientId));
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/patient/me")
    public ResponseEntity<List<PrescriptionDrug>> listMyDiagnosis(@RequestHeader("Authorization") String authHeader) {

        String token = authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : authHeader;

        UserDetails userDetails = jwtService.getUserFromToken(token);
        if (!jwtService.isTokenValid(token, userDetails)) {
            throw new RuntimeException("Invalid or expired JWT");
        }
        if (userDetails instanceof Patient patient)
        {
            List<PrescriptionDrug> list = service.findByPatientId(patient.getUserId());
            return ResponseEntity.ok(list);
        }


        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/add")
    public ResponseEntity<PrescriptionDrug> addPrescriptionDrug(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody PrescriptionDrugRequest dto) {

        String token = authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : authHeader;

        UserDetails userDetails = jwtService.getUserFromToken(token);
        if (!jwtService.isTokenValid(token, userDetails)) {
            throw new RuntimeException("Invalid or expired JWT");
        }
        Prescription presc = prescriptionRepo.findById(dto.getPrescriptionId())
                .orElseThrow(() -> new RuntimeException("No Prescription id=" + dto.getPrescriptionId()));


        Drug drug = drugRepo.findById(dto.getDrugId())
                .orElseThrow(() -> new RuntimeException("No Drug id=" + dto.getDrugId()));


        PrescriptionDrug pd = new PrescriptionDrug();
        pd.setPrescription(presc);
        pd.setDrug(drug);
        pd.setDuration(dto.getDuration());
        pd.setDosage(dto.getDosage());
        pd.setNotes(dto.getNotes());


        PrescriptionDrug saved = service.create(pd);
        return ResponseEntity.ok(saved);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PrescriptionDrug> patchPrescriptionDrug(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        String token = authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : authHeader;

        UserDetails userDetails = jwtService.getUserFromToken(token);
        if (!jwtService.isTokenValid(token, userDetails)) {
            throw new RuntimeException("Invalid or expired JWT");
        }
        PrescriptionDrug existing = service.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No PrescriptionDrug with id=" + id));


        if (updates.containsKey("duration")) {
            existing.setDuration((String)updates.get("duration"));
        }
        if (updates.containsKey("dosage")) {
            existing.setDosage((String)updates.get("dosage"));
        }
        if (updates.containsKey("notes")) {
            existing.setNotes((String)updates.get("notes"));
        }

        PrescriptionDrug saved = service.save(existing);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/prescription/{prescriptionId}")
    public ResponseEntity<List<PrescriptionDrug>> getByPrescriptionId(@PathVariable Long prescriptionId) {
        List<PrescriptionDrug> prescriptionDrugs = service.findByPrescriptionId(prescriptionId);
        return ResponseEntity.ok(prescriptionDrugs);
    }

}
