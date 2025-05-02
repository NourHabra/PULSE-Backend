package com.pulse.chronicdisease.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pulse.chronicdisease.model.PatientChronicDisease;

public interface PatientChronicDiseaseRepository extends JpaRepository<PatientChronicDisease, Long> {
}