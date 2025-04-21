package com.pulse.saving.repository;

import com.pulse.saving.model.SavedPharmacy;
import com.pulse.user.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SavedPharmacyRepository extends JpaRepository<SavedPharmacy, Long> {
    List<SavedPharmacy> findByPatient(Patient patient);
}
