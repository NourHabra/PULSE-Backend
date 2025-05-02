package com.pulse.test.service;


import com.pulse.test.model.LaboratoryTest;
import com.pulse.test.repository.LaboratoryTestRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LaboratoryTestService {
    private final LaboratoryTestRepository repo;
    public LaboratoryTestService(LaboratoryTestRepository repo) {
        this.repo = repo;
    }
    public List<LaboratoryTest> findByLabId(Long labId) {
        return repo.findByLaboratory_LaboratoryId(labId);
    }
    public List<LaboratoryTest> findByTestId(Long testId) {
        return repo.findByTest_TestId(testId);
    }
    public LaboratoryTest create(LaboratoryTest lt) {
        return repo.save(lt);
    }
    public Optional<LaboratoryTest> findById(Long id) {
        return repo.findById(id);
    }
    public LaboratoryTest update(LaboratoryTest lt) {
        return repo.save(lt);
    }
    public void delete(Long id) {
        repo.deleteById(id);
    }
}