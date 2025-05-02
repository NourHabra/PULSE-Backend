package com.pulse.test.controller;


import com.pulse.test.dto.LaboratoryTestRequest;
import com.pulse.test.model.LaboratoryTest;
import com.pulse.test.service.LaboratoryTestService;
import com.pulse.laboratory.model.Laboratory;
import com.pulse.laboratory.repository.LaboratoryRepository;
import com.pulse.test.model.Test;
import com.pulse.test.repository.TestRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/lab-tests")
public class LaboratoryTestController {

    private final LaboratoryTestService service;
    private final LaboratoryRepository labRepo;
    private final TestRepository testRepo;

    public LaboratoryTestController(LaboratoryTestService service,
                                    LaboratoryRepository labRepo,
                                    TestRepository testRepo) {
        this.service = service;
        this.labRepo = labRepo;
        this.testRepo = testRepo;
    }

    // 2) Get all tests offered by a lab
    @GetMapping("/lab/{labId}")
    public ResponseEntity<List<LaboratoryTest>> byLab(@PathVariable Long labId) {
        return ResponseEntity.ok(service.findByLabId(labId));
    }

    // 3) Get all labs offering a test
    @GetMapping("/test/{testId}")
    public ResponseEntity<List<LaboratoryTest>> byTest(@PathVariable Long testId) {
        return ResponseEntity.ok(service.findByTestId(testId));
    }

    // 4) Assign a test to a lab with price
    @PostMapping("/add")
    public ResponseEntity<LaboratoryTest> add(@RequestBody LaboratoryTestRequest dto) {
        Laboratory lab = labRepo.findById(dto.getLabId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Invalid labId"));
        Test test = testRepo.findById(dto.getTestId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Invalid testId"));
        LaboratoryTest lt = new LaboratoryTest(lab, test, dto.getPrice());
        return ResponseEntity.ok(service.create(lt));
    }

    // 5) Update price of existing lab-test
    @PutMapping("/{id}")
    public ResponseEntity<LaboratoryTest> update(
            @PathVariable Long id,
            @RequestBody LaboratoryTestRequest dto) {
        LaboratoryTest existing = service.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No lab-test with id="+id));
        if (dto.getPrice() != null) existing.setPrice(dto.getPrice());
        return ResponseEntity.ok(service.update(existing));
    }

    // 6) Delete lab-test assignment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
