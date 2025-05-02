package com.pulse.test.service;


import com.pulse.test.model.Test;
import com.pulse.test.repository.TestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {

    private final TestRepository repo;

    public TestService(TestRepository repo) {
        this.repo = repo;
    }

    public List<Test> findAll() {
        return repo.findAll();
    }

    public Test findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Test not found: " + id));
    }

    public Test create(Test test) {
        return repo.save(test);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}