package com.pulse.fhir.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.springframework.stereotype.Service;
import org.hl7.fhir.r4.model.IdType;
import org.springframework.beans.factory.annotation.Value;
import com.pulse.fhir.mapper.PatientFhirMapper;
import com.pulse.user.model.Patient;

@Service
public class FhirPatientService {

    private final IGenericClient client;

    public FhirPatientService(FhirContext ctx,
                              @Value("${fhir.base-url}") String serverUrl) {
        this.client = ctx.newRestfulGenericClient(serverUrl);
    }
    public MethodOutcome createInFhir(Patient patient){
        return client.create()
                .resource(PatientFhirMapper.toFhir(patient))
                .execute();
    }

    /* ---------- create / update ---------- */
    public MethodOutcome pushToFhir(Patient patient) {
        org.hl7.fhir.r4.model.Patient fp = PatientFhirMapper.toFhir(patient);

        // PUT /Patient/{18}  → create-or-update
        return client
                .update()
                .resource(fp)
                .execute();
    }


    /* ---------- read ---------- */
    public org.hl7.fhir.r4.model.Patient fetchFromFhir(String id) {
        return client.read()
                .resource(org.hl7.fhir.r4.model.Patient.class)
                .withId(id)
                .execute();
    }

    /* ---------- delete ---------- */
    public MethodOutcome deleteFromFhir(String id) {
        return client.delete()
                .resourceById(new IdType("Patient", id))  // <-- correct form
                .execute();
    }
}
