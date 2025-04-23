package com.pulse.saving.service;

import com.pulse.user.model.Patient;
import com.pulse.user.model.Doctor;
import com.pulse.pharmacy.model.Pharmacy;
import com.pulse.laboratory.model.Laboratory;
import com.pulse.saving.model.SavedDoctor;
import com.pulse.saving.model.SavedPharmacy;
import com.pulse.saving.model.SavedLaboratory;
import com.pulse.saving.repository.SavedDoctorRepository;
import com.pulse.saving.repository.SavedPharmacyRepository;
import com.pulse.saving.repository.SavedLaboratoryRepository;
import com.pulse.user.repository.PatientRepository;
import com.pulse.user.repository.DoctorRepository;
import com.pulse.pharmacy.repository.PharmacyRepository;
import com.pulse.laboratory.repository.LaboratoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SavedEntityService {

    @Autowired
    private SavedDoctorRepository savedDoctorRepository;

    @Autowired
    private SavedPharmacyRepository savedPharmacyRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private SavedLaboratoryRepository savedLaboratoryRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PharmacyRepository pharmacyRepository;

    @Autowired
    private LaboratoryRepository laboratoryRepository;




    public List<SavedDoctor> getSavedDoctors(Patient patient) {
        System.out.println("Fetching saved doctors for patient: " + patient.getUserId());
        List<SavedDoctor> savedDoctors = savedDoctorRepository.findByPatient(patient);
        System.out.println("Fetched saved doctors: " + savedDoctors.size());
        return savedDoctors;
    }

    public List<SavedPharmacy> getSavedPharmacies(Patient patient) {
        System.out.println("Fetching saved pharmacies for patient: " + patient.getUserId());
        List<SavedPharmacy> savedPharmacies = savedPharmacyRepository.findByPatient(patient);
        System.out.println("Fetched saved pharmacies: " + savedPharmacies.size());
        return savedPharmacies;
    }

    public List<SavedLaboratory> getSavedLaboratories(Patient patient) {
        System.out.println("Fetching saved laboratories for patient: " + patient.getUserId());
        List<SavedLaboratory> savedLaboratories = savedLaboratoryRepository.findByPatient(patient);
        System.out.println("Fetched saved laboratories: " + savedLaboratories.size());
        return savedLaboratories;
    }


    @Transactional
    public void saveSavedDoctor(Patient patient, Long doctorId) {
        try {

            Doctor doctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));

            if (patient.getUserId() != null) {
                patient = patientRepository.findById(patient.getUserId())
                        .orElseThrow(() -> new RuntimeException("Patient not found"));
            }
            Optional<SavedDoctor> existingSavedDoctor = savedDoctorRepository.findByPatientAndDoctor(patient, doctor);
            if (existingSavedDoctor.isPresent()) {
                System.out.println("Doctor is already saved for this patient.");
                return;
            }
            SavedDoctor savedDoctor = new SavedDoctor();
            savedDoctor.setPatient(patient);
            savedDoctor.setDoctor(doctor);
            savedDoctorRepository.save(savedDoctor);
        } catch (Exception e) {
            System.out.println("Error in saving doctor: " + e.getMessage());
            throw e;
        }
    }

    @Transactional
    public void saveSavedPharmacy(Patient patient, Long pharmacyId) {
        try {
            Pharmacy pharmacy = pharmacyRepository.findById(pharmacyId)
                    .orElseThrow(() -> new RuntimeException("Pharmacy not found"));

            if (patient.getUserId() != null) {
                patient = patientRepository.findById(patient.getUserId())
                        .orElseThrow(() -> new RuntimeException("Patient not found"));
            }
            Optional<SavedPharmacy> existingSavedPharmacy = savedPharmacyRepository.findByPatientAndPharmacy(patient, pharmacy);
            if (existingSavedPharmacy.isPresent()) {
                System.out.println("Pharmacy is already saved for this patient.");
                return;
            }
            SavedPharmacy savedPharmacy = new SavedPharmacy();
            savedPharmacy.setPatient(patient);
            savedPharmacy.setPharmacy(pharmacy);

            savedPharmacyRepository.save(savedPharmacy);
        } catch (Exception e) {
            System.out.println("Error in saving pharmacy: " + e.getMessage());
            throw e;
        }
    }

    @Transactional
    public void saveSavedLaboratory(Patient patient, Long laboratoryId) {
        try {
            Laboratory laboratory = laboratoryRepository.findById(laboratoryId)
                    .orElseThrow(() -> new RuntimeException("Laboratory not found"));

            if (patient.getUserId() != null) {
                patient = patientRepository.findById(patient.getUserId())
                        .orElseThrow(() -> new RuntimeException("Patient not found"));
            }
            Optional<SavedLaboratory> existingSavedLaboratory = savedLaboratoryRepository.findByPatientAndLaboratory(patient, laboratory);
            if (existingSavedLaboratory.isPresent()) {
                System.out.println("Laboratory is already saved for this patient.");
                return;
            }
            SavedLaboratory savedLaboratory = new SavedLaboratory();
            savedLaboratory.setPatient(patient);
            savedLaboratory.setLaboratory(laboratory);
            savedLaboratoryRepository.save(savedLaboratory);
        } catch (Exception e) {
            System.out.println("Error in saving laboratory: " + e.getMessage());
            throw e;
        }
    }
    @Transactional
    public void deleteSavedDoctor(Patient patient, Long doctorId) {
        try {
            Doctor doctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));
            SavedDoctor savedDoctor = savedDoctorRepository.findByPatientAndDoctor(patient, doctor)
                    .orElseThrow(() -> new RuntimeException("Saved doctor not found"));
            savedDoctorRepository.delete(savedDoctor);
        } catch (Exception e) {
            System.out.println("Error in deleting saved doctor: " + e.getMessage());
            throw e;
        }
    }

    @Transactional
    public void deleteSavedPharmacy(Patient patient, Long pharmacyId) {
        try {

            Pharmacy pharmacy = pharmacyRepository.findById(pharmacyId)
                    .orElseThrow(() -> new RuntimeException("Pharmacy not found"));
            SavedPharmacy savedPharmacy = savedPharmacyRepository.findByPatientAndPharmacy(patient, pharmacy)
                    .orElseThrow(() -> new RuntimeException("Saved pharmacy not found"));

            savedPharmacyRepository.delete(savedPharmacy);
        } catch (Exception e) {
            System.out.println("Error in deleting saved pharmacy: " + e.getMessage());
            throw e;
        }
    }

    @Transactional
    public void deleteSavedLaboratory(Patient patient, Long laboratoryId) {
        try {
            Laboratory laboratory = laboratoryRepository.findById(laboratoryId)
                    .orElseThrow(() -> new RuntimeException("Laboratory not found"));
            SavedLaboratory savedLaboratory = savedLaboratoryRepository.findByPatientAndLaboratory(patient, laboratory)
                    .orElseThrow(() -> new RuntimeException("Saved laboratory not found"));
            savedLaboratoryRepository.delete(savedLaboratory);
        } catch (Exception e) {
            System.out.println("Error in deleting saved laboratory: " + e.getMessage());
            throw e;
        }
    }

}
