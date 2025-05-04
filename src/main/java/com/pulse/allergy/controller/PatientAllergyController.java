package com.pulse.allergy.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.pulse.allergy.model.PatientAllergy;
import com.pulse.allergy.service.PatientAllergyService;
import com.pulse.allergy.dto.PatientAllergyDto;
@RestController
@RequestMapping("/testing/patient-allergies")
public class PatientAllergyController {

    @Autowired
    private PatientAllergyService service;


//    @GetMapping("/me")
//    @PreAuthorize("hasRole('PATIENT')")
//    public List<PatientAllergyDto> getMine(@AuthenticationPrincipal Jwt jwt) {
//        Long patientId = jwt.getClaim("userId");
//        return service.getByPatientId(patientId);
//    }

    @GetMapping("/patient/{patientId}")
    public List<PatientAllergyDto> getByPatient(@PathVariable Long patientId) {
        return service.getByPatientId(patientId);
    }


    @PostMapping
    public PatientAllergy create(@RequestBody PatientAllergy pa) {
        return service.create(pa);
    }



    @PutMapping("/{id}")
    public PatientAllergyDto update(@PathVariable Long id, @RequestBody PatientAllergy pa) {
        return service.update(id, pa);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}