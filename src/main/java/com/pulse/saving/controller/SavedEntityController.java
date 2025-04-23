package com.pulse.saving.controller;

import com.pulse.user.model.Patient;
import com.pulse.saving.service.SavedEntityService;
import com.pulse.security.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.pulse.saving.model.SavedDoctor;
import com.pulse.saving.model.SavedPharmacy;
import com.pulse.saving.model.SavedLaboratory;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/patient/saved")
public class SavedEntityController {

    private final SavedEntityService savedEntityService;
    private final JwtService jwtService;

    @Autowired
    public SavedEntityController(SavedEntityService savedEntityService, JwtService jwtService) {
        this.savedEntityService = savedEntityService;
        this.jwtService = jwtService;
    }



    @GetMapping("/doctor")
    public ResponseEntity<List<SavedDoctor>> getSavedDoctors(@RequestHeader("Authorization") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        Patient patient = (Patient) jwtService.getUserFromToken(jwtToken);
        if (patient == null) {
            throw new RuntimeException("Invalid token or user is not a patient");
        }


        List<SavedDoctor> savedDoctors = savedEntityService.getSavedDoctors(patient);
        System.out.println("Returning saved doctors: " + savedDoctors.size());

        return ResponseEntity.ok(savedDoctors);
    }


    @GetMapping("/pharmacy")
    public ResponseEntity<List<SavedPharmacy>> getSavedPharmacies(@RequestHeader("Authorization") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        Patient patient = (Patient) jwtService.getUserFromToken(jwtToken);
        if (patient == null) {
            throw new RuntimeException("Invalid token or user is not a patient");
        }

        List<SavedPharmacy> savedPharmacies = savedEntityService.getSavedPharmacies(patient);
        System.out.println("Returning saved pharmacies: " + savedPharmacies.size());

        return ResponseEntity.ok(savedPharmacies);
    }

    @GetMapping("/laboratory")
    public ResponseEntity<List<SavedLaboratory>> getSavedLaboratories(@RequestHeader("Authorization") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        Patient patient = (Patient) jwtService.getUserFromToken(jwtToken);
        if (patient == null) {
            throw new RuntimeException("Invalid token or user is not a patient");
        }

        List<SavedLaboratory> savedLaboratories = savedEntityService.getSavedLaboratories(patient);
        System.out.println("Returning saved laboratories: " + savedLaboratories.size());
        return ResponseEntity.ok(savedLaboratories);
    }





    @PostMapping("/doctor")
    public ResponseEntity<String> addSavedDoctor(@RequestBody Map<String, Long> request, @RequestHeader("Authorization") String token) {

        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        Patient patient = (Patient) jwtService.getUserFromToken(jwtToken);

        if (patient == null) {
            throw new RuntimeException("Invalid token or user is not a patient");
        }

        Long doctorId = request.get("doctorId");

        savedEntityService.saveSavedDoctor(patient, doctorId);

        return ResponseEntity.ok("Doctor saved successfully");
    }
    @PostMapping("/pharmacy")
    public ResponseEntity<String> addSavedPharmacy( @RequestBody Map<String, Long> request,@RequestHeader("Authorization") String token) {

        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        Patient patient = (Patient) jwtService.getUserFromToken(jwtToken);
        if (patient == null) {
            throw new RuntimeException("Invalid token or user is not a patient");
        }
        Long pharmacyId = request.get("pharmacyId");

        try {
            savedEntityService.saveSavedPharmacy(patient, pharmacyId);
        } catch (RuntimeException e) {
            System.out.println("Error during pharmacy save: " + e.getMessage());
            throw e;
        }

        return ResponseEntity.ok("Pharmacy saved successfully");
    }
    @PostMapping("/laboratory")
    public ResponseEntity<String> addSavedLaboratory(@RequestBody Map<String, Long> request,@RequestHeader("Authorization") String token) {

        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        Patient patient = (Patient) jwtService.getUserFromToken(jwtToken);
        if (patient == null) {
            throw new RuntimeException("Invalid token or user is not a patient");
        }

        Long laboratoryId = request.get("laboratoryId");

        System.out.println("Laboratory ID to save: " + laboratoryId);

        try {
            savedEntityService.saveSavedLaboratory(patient, laboratoryId);
            System.out.println("Laboratory saved successfully for patient ID: " + patient.getUserId());
        } catch (RuntimeException e) {
            System.out.println("Error during laboratory save: " + e.getMessage());
            throw e;
        }

        return ResponseEntity.ok("Laboratory saved successfully");
    }



    @DeleteMapping("/doctor")
    public ResponseEntity<String> deleteSavedDoctor(@RequestBody Map<String, Long> request, @RequestHeader("Authorization") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        Patient patient = (Patient) jwtService.getUserFromToken(jwtToken);
        if (patient == null) {
            throw new RuntimeException("Invalid token or user is not a patient");
        }

        Long doctorId = request.get("doctorId");

        try {
            savedEntityService.deleteSavedDoctor(patient, doctorId);
            return ResponseEntity.ok("Doctor deleted successfully");
        } catch (RuntimeException e) {
            System.out.println("Error during doctor deletion: " + e.getMessage());
            throw e;  // Rethrow the exception
        }
    }
    @DeleteMapping("/pharmacy")
    public ResponseEntity<String> deleteSavedPharmacy(@RequestBody Map<String, Long> request, @RequestHeader("Authorization") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        Patient patient = (Patient) jwtService.getUserFromToken(jwtToken);
        if (patient == null) {
            throw new RuntimeException("Invalid token or user is not a patient");
        }

        Long pharmacyId = request.get("pharmacyId");

        try {
            savedEntityService.deleteSavedPharmacy(patient, pharmacyId);
            return ResponseEntity.ok("Pharmacy deleted successfully");
        } catch (RuntimeException e) {
            System.out.println("Error during pharmacy deletion: " + e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/laboratory")
    public ResponseEntity<String> deleteSavedLaboratory(@RequestBody Map<String, Long> request, @RequestHeader("Authorization") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        Patient patient = (Patient) jwtService.getUserFromToken(jwtToken);
        if (patient == null) {
            throw new RuntimeException("Invalid token or user is not a patient");
        }

        Long laboratoryId = request.get("laboratoryId");

        try {
            savedEntityService.deleteSavedLaboratory(patient, laboratoryId);
            return ResponseEntity.ok("Laboratory deleted successfully");
        } catch (RuntimeException e) {
            System.out.println("Error during laboratory deletion: " + e.getMessage());
            throw e;
        }
    }

}
