package com.pulse.laboratory.controller;

import com.pulse.laboratory.model.Laboratory;
import com.pulse.laboratory.service.LaboratoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/labs")
public class LaboratoryController {

    private final LaboratoryService laboratoryService;

    public LaboratoryController(LaboratoryService laboratoryService) {
        this.laboratoryService = laboratoryService;
    }

    // CREATE
    @PostMapping("/add")
    public ResponseEntity<Laboratory> createLab(@RequestBody Laboratory lab) {
        return ResponseEntity.ok(laboratoryService.createLab(lab));
    }

    // READ all
    @GetMapping
    public ResponseEntity<List<Laboratory>> getAllLabs() {
        return ResponseEntity.ok(laboratoryService.getAllLabs());
    }

    // READ by ID
    @GetMapping("/{id}")
    public ResponseEntity<Laboratory> getLabById(@PathVariable Long id) {
        return laboratoryService.getLabById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Laboratory> updateLab(
            @PathVariable Long id,
            @RequestBody Laboratory updatedLab
    ) {
        return ResponseEntity.ok(laboratoryService.updateLab(id, updatedLab));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLab(@PathVariable Long id) {
        laboratoryService.deleteLab(id);
        return ResponseEntity.noContent().build();
    }
}
