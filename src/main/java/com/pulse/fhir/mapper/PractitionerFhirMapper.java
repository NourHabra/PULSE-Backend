package com.pulse.fhir.mapper;

import com.pulse.user.model.User;
import com.pulse.user.model.Doctor;
import com.pulse.user.model.EmergencyWorker;
import com.pulse.user.model.HealthEmployee;
import com.pulse.user.model.LabTechnician;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Practitioner;



import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.StringType;

import java.time.ZoneId;
import java.util.Date;

public class PractitionerFhirMapper {


    public static Practitioner toFhir(User base) {

        Practitioner fp = new Practitioner();



        fp.addIdentifier()
                .setSystem("mailto")
                .setValue(base.getEmail());


        fp.addName()
                .setFamily(base.getLastName())
                .addGiven(base.getFirstName());

        if (base.getMobileNumber() != null) {
            fp.addTelecom(new ContactPoint()
                    .setSystem(ContactPoint.ContactPointSystem.PHONE)
                    .setValue(base.getMobileNumber()));
        }


        if (base.getDateOfBirth() != null) {
            Date dob = Date.from(base.getDateOfBirth()
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant());
            fp.setBirthDateElement(new DateType(dob));
        }

        if (base.getGender() != null) {
            fp.setGender(Enumerations.AdministrativeGender.fromCode(
                    base.getGender().toLowerCase()));
        }


        if (base.getAddress() != null) {
            fp.addAddress(new Address().setText(base.getAddress()));
        }
        if (base.getPictureUrl() != null) {
            fp.setPhoto(
                    java.util.List.of(new Attachment().setUrl(base.getPictureUrl())));
        }


        if (base instanceof Doctor d) {
            fp.setId("doc-" + base.getUserId());
            if (d.getLicenseNumber() != null) {
                fp.addIdentifier()
                        .setSystem("urn:license:doctor")
                        .setValue(d.getLicenseNumber());
            }


            if (d.getSpecialization() != null) {
                fp.addQualification()
                        .setCode(code(d.getSpecialization()));
            }

            addExt(fp, "workingHours", d.getWorkingHours());
            addExt(fp, "biography",     d.getBiography());
            addExt(fp, "coordinates",   d.getCoordinates());

        } else if (base instanceof LabTechnician lt) {
            fp.setId("lab-tech-" + base.getUserId());
            if (lt.getLicenseNumber() != null) {
                fp.addIdentifier()
                        .setSystem("urn:license:labtech")
                        .setValue(lt.getLicenseNumber());
            }

            addExt(fp, "technicianRole", lt.getTechnicianRole());

            if (lt.getWorkingLabId() != null) {
                addExt(fp, "workingLab", lt.getWorkingLabId().toString());
            }

        } else if (base instanceof EmergencyWorker ew) {
            fp.setId("emerg-" + base.getUserId());
            if (ew.getLicenseNumber() != null) {
                fp.addIdentifier()
                        .setSystem("urn:license:emergency worker")
                        .setValue(ew.getLicenseNumber());
            }

        } else if (base instanceof HealthEmployee he) {
            fp.setId("health-e-" + base.getUserId());
            if (he.getAuthorizedBy() != null) {
                addExt(fp, "authorizedBy", he.getAuthorizedBy().getUserId().toString());
            }

        }

        return fp;
    }


    private static void addExt(Practitioner fp, String name, String val) {
        if (val != null) {
            fp.addExtension(new Extension(
                    "https://pulse.com/fhir/StructureDefinition/" + name,
                    new StringType(val)));
        }
    }


    private static CodeableConcept code(String display) {
        return new CodeableConcept().setText(display);
    }
}