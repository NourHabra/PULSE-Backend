package com.pulse.fhir.mapper;

import com.pulse.user.model.Patient;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Component;
import java.time.ZoneId;
import java.util.Date;

@Component
public class PatientFhirMapper implements FhirMapper<Patient, org.hl7.fhir.r4.model.Patient> {

    @Override
    public org.hl7.fhir.r4.model.Patient toFhir(Patient patient) {
        org.hl7.fhir.r4.model.Patient fhirPatient = new org.hl7.fhir.r4.model.Patient();

        // Set basic information
        fhirPatient.setId("pat-" + patient.getUserId());

        // Set name
        HumanName name = new HumanName();
        name.setUse(HumanName.NameUse.OFFICIAL);
        name.setFamily(patient.getLastName());
        name.addGiven(patient.getFirstName());
        fhirPatient.addName(name);

        // Set gender
        if (patient.getGender() != null) {
            fhirPatient.setGender(Enumerations.AdministrativeGender.valueOf(patient.getGender().toUpperCase()));
        }

        // Set birth date
        if (patient.getDateOfBirth() != null) {
            fhirPatient.setBirthDate(Date.from(patient.getDateOfBirth()
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()));
        }

        // Set address
        if (patient.getAddress() != null) {
            Address address = new Address();
            address.setUse(Address.AddressUse.HOME);
            address.setType(Address.AddressType.PHYSICAL);
            address.setText(patient.getAddress());
            fhirPatient.addAddress(address);
        }

        // Set phone
        if (patient.getMobileNumber() != null) {
            ContactPoint phone = new ContactPoint();
            phone.setSystem(ContactPoint.ContactPointSystem.PHONE);
            phone.setValue(patient.getMobileNumber());
            phone.setUse(ContactPoint.ContactPointUse.MOBILE);
            fhirPatient.addTelecom(phone);
        }

        // Set email
        if (patient.getEmail() != null) {
            ContactPoint email = new ContactPoint();
            email.setSystem(ContactPoint.ContactPointSystem.EMAIL);
            email.setValue(patient.getEmail());
            email.setUse(ContactPoint.ContactPointUse.HOME);
            fhirPatient.addTelecom(email);
        }

        // Add meta information
        Meta meta = new Meta();
        meta.setVersionId("1");
        meta.setLastUpdated(new Date());
        meta.addProfile("http://hl7.org/fhir/StructureDefinition/Patient");
        fhirPatient.setMeta(meta);

        return fhirPatient;
    }

    @Override
    public Patient fromFhir(org.hl7.fhir.r4.model.Patient fhirPatient) {
        Patient patient = new Patient();

        // Extract ID
        if (fhirPatient.getId() != null) {
            patient.setUserId(Long.parseLong(fhirPatient.getId().replace("pat-", "")));
        }

        // Extract name
        if (!fhirPatient.getName().isEmpty()) {
            HumanName name = fhirPatient.getNameFirstRep();
            if (name.hasGiven()) {
                patient.setFirstName(name.getGivenAsSingleString());
            }
            if (name.hasFamily()) {
                patient.setLastName(name.getFamily());
            }
        }

        // Extract gender
        if (fhirPatient.hasGender()) {
            patient.setGender(fhirPatient.getGender().toCode());
        }

        // Extract birth date
        if (fhirPatient.hasBirthDate()) {
            patient.setDateOfBirth(fhirPatient.getBirthDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        }

        // Extract address
        if (!fhirPatient.getAddress().isEmpty()) {
            patient.setAddress(fhirPatient.getAddressFirstRep().getText());
        }

        // Extract phone
        if (!fhirPatient.getTelecom().isEmpty()) {
            for (ContactPoint telecom : fhirPatient.getTelecom()) {
                if (telecom.getSystem() == ContactPoint.ContactPointSystem.PHONE) {
                    patient.setMobileNumber(telecom.getValue());
                    break;
                }
            }
        }

        // Extract email
        if (!fhirPatient.getTelecom().isEmpty()) {
            for (ContactPoint telecom : fhirPatient.getTelecom()) {
                if (telecom.getSystem() == ContactPoint.ContactPointSystem.EMAIL) {
                    patient.setEmail(telecom.getValue());
                    break;
                }
            }
        }

        return patient;
    }
}
