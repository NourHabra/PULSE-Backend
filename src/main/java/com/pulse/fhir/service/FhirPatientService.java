package com.pulse.fhir.service;

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.pulse.fhir.mapper.PatientFhirMapper;
import com.pulse.user.model.Patient;
import com.pulse.user.service.PatientService;
import com.pulse.user.dto.PatientRegisterDto;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;

@Service
public class FhirPatientService {
    private final FhirContext fhirContext;
    private final IGenericClient client;
    private final FhirValidationService validationService;
    private final PatientFhirMapper patientFhirMapper;
    private final PatientService patientService;

    @Autowired
    public FhirPatientService(
            FhirValidationService validationService,
            PatientFhirMapper patientFhirMapper,
            PatientService patientService) {
        this.validationService = validationService;
        this.patientFhirMapper = patientFhirMapper;
        this.patientService = patientService;
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
                String validationMessage = validationService.getValidationMessages(fhirPatient);
                throw new InvalidRequestException("Invalid FHIR Patient resource: " + validationMessage);
            }
            return fhirPatient;
        } catch (Exception e) {
            throw new InvalidRequestException("Error processing FHIR Patient resource: " + e.getMessage());
        }
    }

    public Patient fromFhir(org.hl7.fhir.r4.model.Patient fhirPatient) {
        try {
            if (!validationService.isValid(fhirPatient)) {
                String validationMessage = validationService.getValidationMessages(fhirPatient);
                throw new InvalidRequestException("Invalid FHIR Patient resource: " + validationMessage);
            }
            return patientFhirMapper.fromFhir(fhirPatient);
        } catch (Exception e) {
            throw new InvalidRequestException("Error processing FHIR Patient resource: " + e.getMessage());
        }
    }

    public Patient savePatient(Patient patient) {
        // Create a PatientRegisterDto from the Patient
        PatientRegisterDto dto = new PatientRegisterDto();
        dto.setFirstName(patient.getFirstName());
        dto.setLastName(patient.getLastName());
        dto.setEmail(patient.getEmail());
        dto.setPassword(patient.getPassword()); // Note: This should be handled securely
        dto.setHeight(patient.getHeight());
        dto.setWeight(patient.getWeight());
        dto.setBloodType(patient.getBloodType());
        dto.setGender(patient.getGender());
        dto.setMobileNumber(patient.getMobileNumber());
        dto.setDateOfBirth(patient.getDateOfBirth());
        dto.setPlaceOfBirth(patient.getPlaceOfBirth());
        dto.setAddress(patient.getAddress());
        dto.setPictureUrl(patient.getPictureUrl());

        return patientService.register(dto);
    }

    public void deleteFromFhir(String fhirId) {
        client.delete()
                .resourceById("Patient", fhirId)
                .execute();
    }
}
