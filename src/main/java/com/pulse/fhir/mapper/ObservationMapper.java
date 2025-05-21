package com.pulse.fhir.mapper;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.*;

import com.pulse.vital.model.PatientVital;
import com.pulse.vital.model.Vital;
import java.util.Date;

public class ObservationMapper {

    public static Observation toFhirObservation(PatientVital patientVital) {
        Observation observation = new Observation();

        observation.setId("vital-" + patientVital.getId());
        observation.getSubject().setReference("Patient/pat-" + patientVital.getPatient().getUserId());

        observation.setStatus(Observation.ObservationStatus.FINAL);

        // Use Vital name and description
        Vital vital = patientVital.getVital();

        if (vital != null) {
            CodeableConcept code = new CodeableConcept();
            code.setText(vital.getName());
            if (vital.getDescription() != null) {
                code.addCoding().setDisplay(vital.getDescription());
            }
            observation.setCode(code);
        }

        // Set effective date/time
        if (patientVital.getTimestamp() != null) {
            Date date = Date.from(patientVital.getTimestamp().atZone(java.time.ZoneId.systemDefault()).toInstant());
            observation.setEffective(new DateTimeType(date));
        }

        // Set value (try to parse as number, fallback to string)
        String measurement = patientVital.getMeasurement();
        if (measurement != null) {
            try {
                double val = Double.parseDouble(measurement);
                Quantity quantity = new Quantity();
                quantity.setValue(val);
                // Use normalValue from Vital entity as unit if available
                if (vital != null && vital.getNormalValue() != null) {
                    quantity.setUnit(vital.getNormalValue());
                }
                observation.setValue(quantity);
            } catch (NumberFormatException e) {
                observation.setValue(new StringType(measurement));
            }
        }

        return observation;
    }

    public static String toFhirJson(PatientVital patientVital, FhirContext fhirContext) {
        Observation observation = toFhirObservation(patientVital);
        return fhirContext.newJsonParser().encodeResourceToString(observation);
    }
}
