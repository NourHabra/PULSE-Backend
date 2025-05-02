package com.pulse.allergy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pulse.allergy.model.PatientAllergy;

public interface PatientAllergyRepository extends JpaRepository<PatientAllergy, Long> {
}