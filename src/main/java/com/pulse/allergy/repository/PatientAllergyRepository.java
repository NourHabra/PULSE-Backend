package com.pulse.allergy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pulse.allergy.model.PatientAllergy;

import java.util.List;

public interface PatientAllergyRepository extends JpaRepository<PatientAllergy, Long> {
    List<PatientAllergy> findByPatient_UserId(Long userId);

}