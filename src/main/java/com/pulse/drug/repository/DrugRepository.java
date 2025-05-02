package com.pulse.drug.repository;


import com.pulse.drug.model.Drug;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrugRepository extends JpaRepository<Drug, Long> {}

