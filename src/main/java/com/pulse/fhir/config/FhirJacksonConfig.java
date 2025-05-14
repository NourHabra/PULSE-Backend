package com.pulse.fhir.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hl7.fhir.r4.model.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class FhirJacksonConfig {

    @Bean
    public MappingJackson2HttpMessageConverter fhirMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = new ObjectMapper();

        // Configure the ObjectMapper for FHIR resources
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Add mixin to handle FHIR Reference class
        mapper.addMixIn(Resource.class, FhirResourceMixin.class);

        converter.setObjectMapper(mapper);
        return converter;
    }
}