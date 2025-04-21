package com.pulse.saving.repository;

import com.pulse.saving.model.SavedDoctor;
import com.pulse.user.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SavedDoctorRepository extends JpaRepository<SavedDoctor, Long> {
    List<SavedDoctor> findByPatient(Patient patient);
}
