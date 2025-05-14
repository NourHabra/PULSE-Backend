package com.pulse.fhir.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hl7.fhir.r4.model.Reference;

public abstract class FhirResourceMixin {
    @JsonIgnore
    public abstract void setReferenceElement(org.hl7.fhir.instance.model.api.IIdType value);

    @JsonProperty("reference")
    public abstract void setReference(String value);
}