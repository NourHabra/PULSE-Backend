package com.pulse.pharmacy.controller;

import com.pulse.pharmacy.model.Pharmacy;
import com.pulse.pharmacy.service.PharmacyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pharmacies")
public class PharmacyController {

    private final PharmacyService pharmacyService;

    public PharmacyController(PharmacyService pharmacyService) {
        this.pharmacyService = pharmacyService;
    }

    // CREATE
    @PostMapping("/add")
    public ResponseEntity<Pharmacy> createPharmacy(@RequestBody Pharmacy pharmacy) {
        return ResponseEntity.ok(pharmacyService.createPharmacy(pharmacy));
    }

    // READ ALL
    // Get all pharmacies
    @GetMapping
    public ResponseEntity<List<Pharmacy>> getAllPharmacies() {
        return ResponseEntity.ok(pharmacyService.getAllPharmacies());
    }

    // Get pharmacy by ID
    @GetMapping("/{id}")
    public ResponseEntity<Pharmacy> getPharmacyById(@PathVariable Long id) {
        return pharmacyService.getPharmacyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Pharmacy> updatePharmacy(@PathVariable Long id, @RequestBody Pharmacy pharmacy) {
        return ResponseEntity.ok(pharmacyService.updatePharmacy(id, pharmacy));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePharmacy(@PathVariable Long id) {
        pharmacyService.deletePharmacy(id);
        return ResponseEntity.noContent().build();
    }



    @GetMapping("/{id}/coordinates/embed")
    public ResponseEntity<String> getPharmacyCoordinatesEmbedLink(@PathVariable Long id) {
        String embedLink = pharmacyService.getPharmacyCoordinatesEmbedLink(id);
        return ResponseEntity.ok(embedLink);
    }
}
