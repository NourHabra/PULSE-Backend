package com.pulse.saving.repository;


import com.pulse.pharmacy.model.Pharmacy;
import com.pulse.saving.model.SavedPharmacy;

import com.pulse.user.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SavedPharmacyRepository extends JpaRepository<SavedPharmacy, Long> {
    List<SavedPharmacy> findByPatient(Patient patient);
    Optional<SavedPharmacy> findByPatientAndPharmacy(Patient patient, Pharmacy pharmacy);
}
