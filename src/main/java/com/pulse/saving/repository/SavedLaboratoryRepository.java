package com.pulse.saving.repository;

import com.pulse.saving.model.SavedLaboratory;
import com.pulse.user.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SavedLaboratoryRepository extends JpaRepository<SavedLaboratory, Long> {
    List<SavedLaboratory> findByPatient(Patient patient);
}
