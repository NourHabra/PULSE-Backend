package com.pulse.fhir.mapper;

import com.pulse.user.model.Patient;
import org.hl7.fhir.r4.model.*;
import java.time.ZoneId;
import java.util.Date;
public class PatientFhirMapper {



        public static org.hl7.fhir.r4.model.Patient toFhir(com.pulse.user.model.Patient p) {

            org.hl7.fhir.r4.model.Patient fp = new org.hl7.fhir.r4.model.Patient();

            /* ----- required id / identifiers ----- */
            fp.setId("pat-" + p.getUserId());                     // pat-20 etc.
            fp.addIdentifier().setValue(p.getEmail());            // email as identifier

            /* ----- human name ----- */
            fp.addName()
                    .setFamily(p.getLastName())
                    .addGiven(p.getFirstName());

            /* ----- simple 1-to-1 properties ----- */
            if (p.getGender() != null)
                fp.setGender(Enumerations.AdministrativeGender.fromCode(
                        p.getGender().toLowerCase()));            // “male”, “female”, “other”, “unknown”

            if (p.getDateOfBirth() != null) {
                Date dob = Date.from(
                        p.getDateOfBirth()
                                .atStartOfDay(ZoneId.systemDefault())
                                .toInstant());
                fp.setBirthDate(dob);
            }

            if (p.getMobileNumber() != null)
                fp.addTelecom(
                        new ContactPoint()
                                .setSystem(ContactPoint.ContactPointSystem.PHONE)
                                .setValue(p.getMobileNumber()));

            if (p.getAddress() != null)
                fp.addAddress(
                        new Address().setText(p.getAddress()));

            if (p.getPictureUrl() != null)
                fp.setPhoto(
                        java.util.List.of(
                                new Attachment().setUrl(p.getPictureUrl())
                        ));

            /* ----- custom extensions for domain-specific data ----- */
            if (p.getHeight() != null)
                fp.addExtension(new Extension(
                        "height",
                        new DecimalType(p.getHeight())));

            if (p.getWeight() != null)
                fp.addExtension(new Extension(
                        "weight",
                        new DecimalType(p.getWeight())));

            if (p.getBloodType() != null)
                fp.addExtension(new Extension(
                        "blood-type",
                        new StringType(p.getBloodType())));


            return fp;
        }


        /* FHIR ➜ App (optional) --------------------------------------------- */
    public static Patient fromFhir(org.hl7.fhir.r4.model.Patient fp) {
        Patient p = new Patient();
        p.setUserId(Long.parseLong(fp.getIdElement().getIdPart()));
        p.setEmail(fp.getIdentifierFirstRep().getValue());
        p.setFirstName(fp.getNameFirstRep().getGivenAsSingleString());
        p.setLastName(fp.getNameFirstRep().getFamily());
        // …extract extensions if needed…
        return p;
    }
}
