package com.pulse.vital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pulse.vital.model.Vital;

public interface VitalRepository extends JpaRepository<Vital, Long> {
}