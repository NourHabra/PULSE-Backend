package com.pulse.saving.repository;

import com.pulse.laboratory.model.Laboratory;
import com.pulse.saving.model.SavedLaboratory;
import com.pulse.user.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SavedLaboratoryRepository extends JpaRepository<SavedLaboratory, Long> {
    List<SavedLaboratory> findByPatient(Patient patient);
    Optional<SavedLaboratory> findByPatientAndLaboratory(Patient patient, Laboratory laboratory);

}
