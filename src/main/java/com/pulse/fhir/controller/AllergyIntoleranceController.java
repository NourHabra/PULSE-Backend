package com.pulse.fhir.controller;

import ca.uhn.fhir.context.FhirContext;
import com.pulse.fhir.mapper.AllergyIntoleranceMapper;
import com.pulse.allergy.model.PatientAllergy;
import com.pulse.allergy.repository.PatientAllergyRepository;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/fhir/AllergyIntolerance")
public class AllergyIntoleranceController {

    private final PatientAllergyRepository patientAllergyRepository;
    private final FhirContext fhirContext;

    public AllergyIntoleranceController(PatientAllergyRepository patientAllergyRepository, FhirContext fhirContext) {
        this.patientAllergyRepository = patientAllergyRepository;
        this.fhirContext = fhirContext;
    }

    @GetMapping(value = "/{patientId}", produces = "application/fhir+json")
    public ResponseEntity<String> getAllergiesByPatient(@PathVariable Long patientId) {
        List<PatientAllergy> allergies = patientAllergyRepository.findByPatient_UserId(patientId);
        System.out.println(allergies.get(0));
        System.out.println(allergies.get(1));
        System.out.println(allergies.get(2));
        System.out.println(allergies.get(3));
        System.out.println("Allergies count: " + allergies.size());

        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.SEARCHSET);
        for (PatientAllergy allergy : allergies) {
            System.out.println("Mapping allergy: " + allergy);
            org.hl7.fhir.r4.model.AllergyIntolerance fhirAllergy = AllergyIntoleranceMapper.toFhir(allergy);
            System.out.println("Mapped FHIR Allergy: " + fhirAllergy);
            bundle.addEntry().setResource(fhirAllergy);
        }


        String json = fhirContext.newJsonParser().encodeResourceToString(bundle);
        return ResponseEntity.ok(json);
    }
}
