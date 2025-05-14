package com.pulse.fhir.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import java.util.Arrays;

@Configuration
public class FhirJacksonConfig {

    @Bean
    public MappingJackson2HttpMessageConverter fhirMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = new ObjectMapper();

        // Configure the ObjectMapper for FHIR resources
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Configure deserialization features
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        // Add mixin to handle FHIR Reference class
        mapper.addMixIn(Reference.class, FhirResourceMixin.class);

        // Add mixin for Resource class to handle resourceType
        mapper.addMixIn(Resource.class, ResourceMixin.class);

        converter.setObjectMapper(mapper);

        // Support FHIR content type
        converter.setSupportedMediaTypes(Arrays.asList(
                MediaType.parseMediaType("application/fhir+json"),
                MediaType.parseMediaType("application/fhir+json;charset=UTF-8"),
                MediaType.APPLICATION_JSON));

        return converter;
    }
}