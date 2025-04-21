package com.pulse.saving.service;

import com.pulse.user.model.Patient;
import com.pulse.saving.model.SavedDoctor;
import com.pulse.saving.model.SavedPharmacy;
import com.pulse.saving.model.SavedLaboratory;
import com.pulse.saving.repository.SavedDoctorRepository;
import com.pulse.saving.repository.SavedPharmacyRepository;
import com.pulse.saving.repository.SavedLaboratoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SavedEntityService {

    @Autowired
    private SavedDoctorRepository savedDoctorRepository;

    @Autowired
    private SavedPharmacyRepository savedPharmacyRepository;

    @Autowired
    private SavedLaboratoryRepository savedLaboratoryRepository;

    // Get saved doctors for a specific patient
    public List<SavedDoctor> getSavedDoctors(Patient patient) {
        return savedDoctorRepository.findByPatient(patient);
    }

    // Get saved pharmacies for a specific patient
    public List<SavedPharmacy> getSavedPharmacies(Patient patient) {
        return savedPharmacyRepository.findByPatient(patient);
    }

    // Get saved laboratories for a specific patient
    public List<SavedLaboratory> getSavedLaboratories(Patient patient) {
        return savedLaboratoryRepository.findByPatient(patient);
    }
}
