package com.pulse.laboratory.repository;

import com.pulse.laboratory.model.Laboratory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LaboratoryRepository extends JpaRepository<Laboratory, Long> {


    Laboratory findByLicenseNumber(String licenseNumber);
}
