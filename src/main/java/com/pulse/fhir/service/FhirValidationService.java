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

    public FhirValidationService() {
        this.fhirContext = FhirContext.forR4();
        this.validator = fhirContext.newValidator();
    }

    public ValidationResult validateResource(Resource resource) {
        return validator.validateWithResult(resource);
    }

    public boolean isValid(Resource resource) {
        ValidationResult result = validateResource(resource);
        return result.isSuccessful();
    }

    public String getValidationMessages(Resource resource) {
        ValidationResult result = validateResource(resource);
        if (result.isSuccessful()) {
            return "Resource is valid";
        }
        return result.getMessages().stream()
                .map(message -> message.getLocationString() + ": " + message.getMessage())
                .reduce((a, b) -> a + "\n" + b)
                .orElse("No validation messages");
    }
}