package com.pulse.allergy.controller;


import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.pulse.allergy.model.Allergy;
import com.pulse.allergy.repository.AllergyRepository;

@RestController
@RequestMapping("/static/allergies")
public class AllergyController {
    @Autowired
    private AllergyRepository repo;

    @GetMapping
    public List<Allergy> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Allergy getOne(@PathVariable Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Allergy not found: " + id));
    }

    @PostMapping
    public Allergy create(@RequestBody Allergy allergy) {
        return repo.save(allergy);
    }

    @PutMapping("/{id}")
    public Allergy update(@PathVariable Long id, @RequestBody Allergy allergy) {
        Allergy existing = getOne(id);
        existing.setAllergen(allergy.getAllergen());
        existing.setType(allergy.getType());
        return repo.save(existing);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
