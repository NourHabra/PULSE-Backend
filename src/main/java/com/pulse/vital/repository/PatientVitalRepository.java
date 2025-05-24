package com.pulse.vital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pulse.vital.model.PatientVital;

import java.util.List;
import java.util.Optional;

public interface PatientVitalRepository extends JpaRepository<PatientVital, Long> {
    List<PatientVital> findByPatient_UserId(Long userId);
    Optional<PatientVital> findById(Long id);

}