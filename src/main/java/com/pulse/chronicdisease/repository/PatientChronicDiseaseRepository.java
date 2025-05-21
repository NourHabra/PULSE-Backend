package com.pulse.chronicdisease.repository;

import com.pulse.allergy.model.PatientAllergy;
import org.springframework.data.jpa.repository.JpaRepository;
import com.pulse.chronicdisease.model.PatientChronicDisease;

import java.util.List;

public interface PatientChronicDiseaseRepository extends JpaRepository<PatientChronicDisease, Long> {

    List<PatientChronicDisease> findByPatient_UserId(Long userId);
}