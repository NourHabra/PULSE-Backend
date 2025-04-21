package com.pulse.saving.controller;

import com.pulse.user.model.Patient;
import com.pulse.saving.model.SavedDoctor;
import com.pulse.saving.model.SavedPharmacy;
import com.pulse.saving.model.SavedLaboratory;
import com.pulse.saving.service.SavedEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient/saved")
public class SavedEntityController {

    @Autowired
    private SavedEntityService savedEntityService;

    // Get saved doctors for a specific patient
    @GetMapping("/doctors")
    public ResponseEntity<List<SavedDoctor>> getSavedDoctors(@RequestHeader("Authorization") String token) {
        Patient patient = getPatientFromToken(token);
        List<SavedDoctor> savedDoctors = savedEntityService.getSavedDoctors(patient);
        return ResponseEntity.ok(savedDoctors);
    }

    // Get saved pharmacies for a specific patient
    @GetMapping("/pharmacies")
    public ResponseEntity<List<SavedPharmacy>> getSavedPharmacies(@RequestHeader("Authorization") String token) {
        Patient patient = getPatientFromToken(token);
        List<SavedPharmacy> savedPharmacies = savedEntityService.getSavedPharmacies(patient);
        return ResponseEntity.ok(savedPharmacies);
    }

    // Get saved laboratories for a specific patient
    @GetMapping("/laboratories")
    public ResponseEntity<List<SavedLaboratory>> getSavedLaboratories(@RequestHeader("Authorization") String token) {
        Patient patient = getPatientFromToken(token);
        List<SavedLaboratory> savedLaboratories = savedEntityService.getSavedLaboratories(patient);
        return ResponseEntity.ok(savedLaboratories);
    }

    // Helper method to extract patient from JWT token
    private Patient getPatientFromToken(String token) {
        // Implement your JWT utility to extract the patient info from the token
        // For now, return a dummy patient object, replace this with actual logic
        // For example, decode the JWT to get the patient ID, then fetch the patient from the database
        Long patientId = 1L;  // Replace with actual logic
        return new Patient();  // Replace with actual logic to fetch the patient from DB
    }
}
