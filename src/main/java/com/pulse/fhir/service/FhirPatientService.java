package com.pulse.fhir.service;

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
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
    private final PatientFhirMapper patientFhirMapper;

    @Autowired
    public FhirPatientService(FhirValidationService validationService, PatientFhirMapper patientFhirMapper) {
        this.validationService = validationService;
        this.patientFhirMapper = patientFhirMapper;
        this.fhirContext = FhirContext.forR4();
        this.client = fhirContext.newRestfulGenericClient("http://hapi.fhir.org/baseR4");

        // Add logging interceptor
        LoggingInterceptor loggingInterceptor = new LoggingInterceptor();
        loggingInterceptor.setLogRequestBody(true);
        loggingInterceptor.setLogResponseBody(true);
        client.registerInterceptor(loggingInterceptor);
    }

    public org.hl7.fhir.r4.model.Patient pushToFhir(Patient patient) {
        org.hl7.fhir.r4.model.Patient fhirPatient = patientFhirMapper.toFhir(patient);

        try {
            if (!validationService.isValid(fhirPatient)) {
                throw new InvalidRequestException("Invalid FHIR Patient resource");
            }
            return fhirPatient;
        } catch (Exception e) {
            throw new InvalidRequestException("Error validating FHIR Patient resource: " + e.getMessage());
        }
    }

    public Patient fromFhir(org.hl7.fhir.r4.model.Patient fhirPatient) {
        return patientFhirMapper.fromFhir(fhirPatient);
    }

    public void deleteFromFhir(String fhirId) {
        client.delete()
                .resourceById("Patient", fhirId)
                .execute();
    }
}
