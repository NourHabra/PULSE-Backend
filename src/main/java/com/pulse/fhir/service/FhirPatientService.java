package com.pulse.fhir.service;

import com.pulse.fhir.mapper.PatientFhirMapper;
import com.pulse.user.model.Patient;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.api.MethodOutcome;

@Service
public class FhirPatientService {
    private final FhirContext fhirContext;
    private final IGenericClient client;
    private final FhirValidationService validationService;

    @Autowired
    public FhirPatientService(FhirValidationService validationService) {
        this.validationService = validationService;
        this.fhirContext = FhirContext.forR4();
        this.client = fhirContext.newRestfulGenericClient("http://hapi.fhir.org/baseR4");

        // Add logging interceptor
        LoggingInterceptor loggingInterceptor = new LoggingInterceptor();
        loggingInterceptor.setLogRequestBody(true);
        loggingInterceptor.setLogResponseBody(true);
        client.registerInterceptor(loggingInterceptor);
    }

    public void pushToFhir(Patient patient) {
        org.hl7.fhir.r4.model.Patient fhirPatient = PatientFhirMapper.toFhir(patient);

        // Validate the FHIR resource
        if (!validationService.isValid(fhirPatient)) {
            String validationMessages = validationService.getValidationMessages(fhirPatient);
            throw new RuntimeException("Invalid FHIR Patient resource: " + validationMessages);
        }

        // Create or update the patient in the FHIR server
        if (fhirPatient.getIdElement() != null && !fhirPatient.getIdElement().isEmpty()) {
            client.update()
                    .resource(fhirPatient)
                    .execute();
        } else {
            client.create()
                    .resource(fhirPatient)
                    .execute();
        }
    }

    public Patient getFromFhir(String fhirId) {
        org.hl7.fhir.r4.model.Patient fhirPatient = client.read()
                .resource(org.hl7.fhir.r4.model.Patient.class)
                .withId(fhirId)
                .execute();

        return PatientFhirMapper.fromFhir(fhirPatient);
    }

    public void deleteFromFhir(String fhirId) {
        client.delete()
                .resourceById("Patient", fhirId)
                .execute();
    }
}
