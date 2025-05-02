package com.pulse.allergy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pulse.allergy.model.Allergy;

public interface AllergyRepository extends JpaRepository<Allergy, Long> {
}