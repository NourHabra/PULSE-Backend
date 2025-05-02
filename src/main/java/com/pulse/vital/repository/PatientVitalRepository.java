package com.pulse.vital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pulse.vital.model.PatientVital;

public interface PatientVitalRepository extends JpaRepository<PatientVital, Long> {
}