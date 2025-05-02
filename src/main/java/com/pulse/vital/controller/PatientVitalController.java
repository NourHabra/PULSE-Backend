package com.pulse.vital.controller;


import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.pulse.vital.dto.PatientVitalDto;
import com.pulse.vital.model.PatientVital;
import com.pulse.vital.service.PatientVitalService;

@RestController
@RequestMapping("/testing/patient-vitals")
public class PatientVitalController {
    @Autowired
    private PatientVitalService service;

    @GetMapping("/patient/{patientId}")
    public List<PatientVitalDto> getByPatient(@PathVariable Long patientId) {
        return service.getByPatientId(patientId);
    }

    @PostMapping
    public PatientVitalDto create(@RequestBody PatientVital pv) {
        return service.create(pv);
    }

    @PutMapping("/{id}")
    public PatientVitalDto update(@PathVariable Long id, @RequestBody PatientVital pv) {
        return service.update(id, pv);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
