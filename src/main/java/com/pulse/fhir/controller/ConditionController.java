package com.pulse.fhir.controller;

import ca.uhn.fhir.context.FhirContext;
import com.pulse.fhir.mapper.ConditionMapper;
import com.pulse.chronicdisease.model.PatientChronicDisease;
import com.pulse.chronicdisease.repository.PatientChronicDiseaseRepository;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Condition;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/fhir/Condition")
public class ConditionController {

    private final PatientChronicDiseaseRepository patientChronicDiseaseRepository;
    private final FhirContext fhirContext;

    public ConditionController(PatientChronicDiseaseRepository patientChronicDiseaseRepository, FhirContext fhirContext) {
        this.patientChronicDiseaseRepository = patientChronicDiseaseRepository;
        this.fhirContext = fhirContext;
    }

    @GetMapping(value = "/{patientId}", produces = "application/fhir+json")
    public ResponseEntity<String> getConditionsByPatient(@PathVariable Long patientId) {
        List<PatientChronicDisease> chronicDiseases = patientChronicDiseaseRepository.findByPatient_UserId(patientId);

        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.SEARCHSET);
        for (PatientChronicDisease disease : chronicDiseases) {
            org.hl7.fhir.r4.model.Condition fhirCondition = ConditionMapper.toFhir(disease);
            bundle.addEntry().setResource(fhirCondition);
        }

        String json = fhirContext.newJsonParser().encodeResourceToString(bundle);
        return ResponseEntity.ok(json);
    }
}
