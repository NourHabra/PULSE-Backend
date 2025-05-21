package com.pulse.fhir.mapper;

import com.pulse.chronicdisease.model.ChronicDisease;
import com.pulse.chronicdisease.model.PatientChronicDisease;
//import org.hl7.fhir.r4.model.Category;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Reference;

import java.util.Date;

public class ConditionMapper {

    public static Condition toFhir(PatientChronicDisease patientChronicDisease) {
        ChronicDisease disease = patientChronicDisease.getChronicDisease();

        Condition condition = new Condition();

        condition.setId("cond-" + patientChronicDisease.getId());

        condition.setClinicalStatus(new CodeableConcept().addCoding(new Coding()
                .setSystem("http://terminology.hl7.org/CodeSystem/condition-clinical")
                .setCode("active")));

        condition.addCategory(new CodeableConcept().addCoding(new Coding()
                .setSystem("http://terminology.hl7.org/CodeSystem/condition-category")
                .setCode("problem-list-item")));

        condition.setCode(new CodeableConcept().setText(disease.getDisease()));

        condition.setSubject(new Reference("Patient/pat-" + patientChronicDisease.getPatient().getUserId()));

        if (patientChronicDisease.getStartDate() != null) {
            condition.setOnset(new org.hl7.fhir.r4.model.DateTimeType(Date.from(
                    patientChronicDisease.getStartDate().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant())));
        }

        return condition;
    }
}
