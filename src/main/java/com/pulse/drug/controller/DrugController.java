package com.pulse.drug.controller;


import com.pulse.drug.model.Drug;
import com.pulse.drug.service.DrugService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api/drugs")
public class DrugController {
    private final DrugService service;
    public DrugController(DrugService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<List<Drug>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Drug> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<Drug> add(@RequestBody Drug drug) {
        return ResponseEntity.ok(service.create(drug));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}