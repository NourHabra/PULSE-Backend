package com.pulse.drug.service;


import com.pulse.drug.model.Drug;
import com.pulse.drug.repository.DrugRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DrugService {
    private final DrugRepository repo;
    public DrugService(DrugRepository repo) { this.repo = repo; }

    public List<Drug> findAll() { return repo.findAll(); }
    public Drug findById(Long id) { return repo.findById(id).orElseThrow(() -> new RuntimeException("Drug not found")); }
    public Drug create(Drug d) { return repo.save(d); }
    public void delete(Long id) { repo.deleteById(id); }
}