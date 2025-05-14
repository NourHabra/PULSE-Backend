package com.pulse.fhir.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hl7.fhir.r4.model.Resource;

public abstract class ResourceMixin {
    @JsonProperty("resourceType")
    public abstract String getResourceType();

    @JsonProperty("resourceType")
    public abstract void setResourceType(String resourceType);
}