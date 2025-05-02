package com.pulse.chronicdisease.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.pulse.chronicdisease.model.ChronicDisease;
import com.pulse.chronicdisease.repository.ChronicDiseaseRepository;

@RestController
@RequestMapping("/static/chronic-diseases")
public class ChronicDiseaseController {
    @Autowired
    private ChronicDiseaseRepository repo;

    @GetMapping
    public List<ChronicDisease> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ChronicDisease getOne(@PathVariable Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("ChronicDisease not found: " + id));
    }

    @PostMapping
    public ChronicDisease create(@RequestBody ChronicDisease cd) {
        return repo.save(cd);
    }

    @PutMapping("/{id}")
    public ChronicDisease update(@PathVariable Long id, @RequestBody ChronicDisease cd) {
        ChronicDisease existing = getOne(id);
        existing.setDisease(cd.getDisease());
        existing.setType(cd.getType());
        return repo.save(existing);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}