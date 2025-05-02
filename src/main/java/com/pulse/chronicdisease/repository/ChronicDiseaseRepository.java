package com.pulse.chronicdisease.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.pulse.chronicdisease.model.ChronicDisease;

public interface ChronicDiseaseRepository extends JpaRepository<ChronicDisease, Long> {
}
