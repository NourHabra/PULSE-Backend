package com.pulse.fhir.mapper;

import org.hl7.fhir.r4.model.Resource;

public interface FhirMapper<T, R extends Resource> {
    R toFhir(T domain);

    T fromFhir(R fhir);
}