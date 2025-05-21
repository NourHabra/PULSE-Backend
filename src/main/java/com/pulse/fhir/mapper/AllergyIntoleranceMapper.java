package com.pulse.fhir.mapper;

import com.pulse.allergy.model.Allergy;
import com.pulse.allergy.model.PatientAllergy;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;

public class AllergyIntoleranceMapper {

    public static AllergyIntolerance toFhir(PatientAllergy patientAllergy) {
        Allergy allergy = patientAllergy.getAllergy();

        AllergyIntolerance ai = new AllergyIntolerance();

        ai.setId("alg-" + patientAllergy.getId());

        ai.setClinicalStatus(new CodeableConcept().addCoding(new Coding()
                .setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical")
                .setCode("active")));

        ai.setVerificationStatus(new CodeableConcept().addCoding(new Coding()
                .setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-verification")
                .setCode("confirmed")));

        // Category example, adjust according to allergy type
        ai.addCategory(AllergyIntolerance.AllergyIntoleranceCategory.FOOD);

        ai.setCode(new CodeableConcept().setText(allergy.getAllergen()));

        ai.setPatient(new Reference("Patient/pat-" + patientAllergy.getPatient().getUserId()));

        // Add reaction severity from intensity
        ai.addReaction()
                .setSeverity(mapSeverity(patientAllergy.getIntensity()));

        return ai;
    }

    private static AllergyIntolerance.AllergyIntoleranceSeverity mapSeverity(String intensity) {
        if (intensity == null) return AllergyIntolerance.AllergyIntoleranceSeverity.MODERATE;

        return switch (intensity.toLowerCase()) {
            case "mild" -> AllergyIntolerance.AllergyIntoleranceSeverity.MILD;
            case "severe" -> AllergyIntolerance.AllergyIntoleranceSeverity.SEVERE;
            default -> AllergyIntolerance.AllergyIntoleranceSeverity.MODERATE;
        };
    }
}
