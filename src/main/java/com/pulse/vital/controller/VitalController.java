package com.pulse.vital.controller;


import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.pulse.vital.model.Vital;
import com.pulse.vital.repository.VitalRepository;

@RestController
@RequestMapping("/static/vitals")
public class VitalController {
    @Autowired
    private VitalRepository repo;

    @GetMapping
    public List<Vital> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Vital getOne(@PathVariable Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Vital not found: " + id));
    }

    @PostMapping
    public Vital create(@RequestBody Vital v) {
        return repo.save(v);
    }

    @PutMapping("/{id}")
    public Vital update(@PathVariable Long id, @RequestBody Vital v) {
        Vital existing = getOne(id);
        existing.setName(v.getName());
        existing.setDescription(v.getDescription());
        existing.setNormalValue(v.getNormalValue());
        return repo.save(existing);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
