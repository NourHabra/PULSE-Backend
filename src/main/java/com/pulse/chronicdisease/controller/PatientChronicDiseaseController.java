package com.pulse.chronicdisease.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.pulse.chronicdisease.dto.PatientChronicDiseaseDto;
import com.pulse.chronicdisease.model.PatientChronicDisease;
import com.pulse.chronicdisease.service.PatientChronicDiseaseService;

@RestController
@RequestMapping("/testing/patient-chronic-diseases")
public class PatientChronicDiseaseController {
    @Autowired
    private PatientChronicDiseaseService service;

    @GetMapping("/patient/{patientId}")
    public List<PatientChronicDiseaseDto> getByPatient(@PathVariable Long patientId) {
        return service.getByPatientId(patientId);
    }

    @PostMapping
    public PatientChronicDiseaseDto create(@RequestBody PatientChronicDisease pcd) {
        return service.create(pcd);
    }

    @PutMapping("/{id}")
    public PatientChronicDiseaseDto update(@PathVariable Long id, @RequestBody PatientChronicDisease pcd) {
        return service.update(id, pcd);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}