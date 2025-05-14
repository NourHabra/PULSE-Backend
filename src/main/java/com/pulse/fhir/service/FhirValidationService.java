package com.pulse.fhir.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.r4.model.Resource;
import org.springframework.stereotype.Service;

@Service
public class FhirValidationService {
    private final FhirContext fhirContext;
    private final FhirValidator validator;

    public FhirValidationService(FhirContext fhirContext) {
        this.fhirContext = fhirContext;
        this.validator = fhirContext.newValidator();
    }

    public ValidationResult validateResource(Resource resource) {
        // Use a simple validation that doesn't require schema
        return validator.validateWithResult(resource);
    }

    public boolean isValid(Resource resource) {
        try {
            // Basic validation - check if required fields are present
            if (resource instanceof org.hl7.fhir.r4.model.Patient) {
                org.hl7.fhir.r4.model.Patient patient = (org.hl7.fhir.r4.model.Patient) resource;
                boolean hasName = patient.hasName() && !patient.getName().isEmpty();
                boolean hasPassword = patient.hasExtension("http://pulse.com/fhir/StructureDefinition/password");
                return hasName && hasPassword;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getValidationMessages(Resource resource) {
        if (!isValid(resource)) {
            if (resource instanceof org.hl7.fhir.r4.model.Patient) {
                org.hl7.fhir.r4.model.Patient patient = (org.hl7.fhir.r4.model.Patient) resource;
                StringBuilder messages = new StringBuilder();
                if (!patient.hasName() || patient.getName().isEmpty()) {
                    messages.append("Patient must have at least one name. ");
                }
                if (!patient.hasExtension("http://pulse.com/fhir/StructureDefinition/password")) {
                    messages.append("Patient must have a password. ");
                }
                return messages.toString().trim();
            }
            return "Resource validation failed";
        }
        return "Resource is valid";
    }
}