package com.pulse.fhir.mapper;

import com.pulse.user.model.Patient;
import org.hl7.fhir.r4.model.*;
import java.time.ZoneId;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class PatientFhirMapper {
    public static org.hl7.fhir.r4.model.Patient toFhir(com.pulse.user.model.Patient p) {
        org.hl7.fhir.r4.model.Patient fp = new org.hl7.fhir.r4.model.Patient();

        // Required FHIR fields
        fp.setId("pat-" + p.getUserId());

        // Add identifier with system and value
        Identifier identifier = new Identifier();
        identifier.setSystem("http://pulse.com/identifiers/patient");
        identifier.setValue(p.getEmail());
        fp.addIdentifier(identifier);

        // Human name with proper structure
        HumanName name = new HumanName();
        name.setFamily(p.getLastName());
        name.addGiven(p.getFirstName());
        name.setUse(HumanName.NameUse.OFFICIAL);
        fp.addName(name);

        // Gender with proper FHIR enum
        if (p.getGender() != null) {
            fp.setGender(Enumerations.AdministrativeGender.fromCode(
                    p.getGender().toLowerCase()));
        }

        // Birth date
        if (p.getDateOfBirth() != null) {
            Date dob = Date.from(
                    p.getDateOfBirth()
                            .atStartOfDay(ZoneId.systemDefault())
                            .toInstant());
            fp.setBirthDate(dob);
        }

        // Contact information
        if (p.getMobileNumber() != null) {
            ContactPoint telecom = new ContactPoint();
            telecom.setSystem(ContactPoint.ContactPointSystem.PHONE);
            telecom.setValue(p.getMobileNumber());
            telecom.setUse(ContactPoint.ContactPointUse.MOBILE);
            fp.addTelecom(telecom);
        }

        // Address
        if (p.getAddress() != null) {
            Address address = new Address();
            address.setText(p.getAddress());
            address.setUse(Address.AddressUse.HOME);
            fp.addAddress(address);
        }

        // Photo
        if (p.getPictureUrl() != null) {
            Attachment photo = new Attachment();
            photo.setUrl(p.getPictureUrl());
            photo.setContentType("image/jpeg");
            fp.setPhoto(List.of(photo));
        }

        // Add extension for custom fields
        if (p.getHeight() != null) {
            Extension heightExt = new Extension();
            heightExt.setUrl("http://pulse.com/fhir/StructureDefinition/height");
            heightExt.setValue(new DecimalType(p.getHeight()));
            fp.addExtension(heightExt);
        }

        if (p.getWeight() != null) {
            Extension weightExt = new Extension();
            weightExt.setUrl("http://pulse.com/fhir/StructureDefinition/weight");
            weightExt.setValue(new DecimalType(p.getWeight()));
            fp.addExtension(weightExt);
        }

        if (p.getBloodType() != null) {
            Extension bloodTypeExt = new Extension();
            bloodTypeExt.setUrl("http://pulse.com/fhir/StructureDefinition/bloodType");
            bloodTypeExt.setValue(new StringType(p.getBloodType()));
            fp.addExtension(bloodTypeExt);
        }

        // Add meta information
        Meta meta = new Meta();
        meta.setVersionId("1");
        meta.setLastUpdated(new Date());
        meta.addProfile("http://hl7.org/fhir/StructureDefinition/Patient");
        fp.setMeta(meta);

        return fp;
    }

    public static com.pulse.user.model.Patient fromFhir(org.hl7.fhir.r4.model.Patient fp) {
        com.pulse.user.model.Patient p = new com.pulse.user.model.Patient();

        // Extract identifier
        if (!fp.getIdentifier().isEmpty()) {
            p.setEmail(fp.getIdentifierFirstRep().getValue());
        }

        // Extract name
        if (!fp.getName().isEmpty()) {
            HumanName name = fp.getNameFirstRep();
            p.setLastName(name.getFamily());
            if (!name.getGiven().isEmpty()) {
                p.setFirstName(name.getGiven().get(0).getValue());
            }
        }

        // Extract gender
        if (fp.hasGender()) {
            p.setGender(fp.getGender().toCode());
        }

        // Extract birth date
        if (fp.hasBirthDate()) {
            p.setDateOfBirth(fp.getBirthDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        }

        // Extract contact information
        if (!fp.getTelecom().isEmpty()) {
            for (ContactPoint telecom : fp.getTelecom()) {
                if (telecom.getSystem() == ContactPoint.ContactPointSystem.PHONE) {
                    p.setMobileNumber(telecom.getValue());
                    break;
                }
            }
        }

        // Extract address
        if (!fp.getAddress().isEmpty()) {
            p.setAddress(fp.getAddressFirstRep().getText());
        }

        // Extract photo
        if (!fp.getPhoto().isEmpty()) {
            p.setPictureUrl(fp.getPhotoFirstRep().getUrl());
        }

        // Extract extensions
        for (Extension ext : fp.getExtension()) {
            switch (ext.getUrl()) {
                case "http://pulse.com/fhir/StructureDefinition/height":
                    if (ext.getValue() instanceof DecimalType) {
                        p.setHeight(((DecimalType) ext.getValue()).getValue().doubleValue());
                    }
                    break;
                case "http://pulse.com/fhir/StructureDefinition/weight":
                    if (ext.getValue() instanceof DecimalType) {
                        p.setWeight(((DecimalType) ext.getValue()).getValue().doubleValue());
                    }
                    break;
                case "http://pulse.com/fhir/StructureDefinition/bloodType":
                    if (ext.getValue() instanceof StringType) {
                        p.setBloodType(((StringType) ext.getValue()).getValue());
                    }
                    break;
            }
        }

        return p;
    }
}
