package com.pulse.fhir.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FhirConfig {
    @Bean
    public FhirContext fhirContext() {
        return FhirContext.forCached(FhirVersionEnum.R4);
    }

}