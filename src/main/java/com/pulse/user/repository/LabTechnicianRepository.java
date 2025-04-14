package com.pulse.user.repository;

import com.pulse.user.model.LabTechnician;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabTechnicianRepository extends JpaRepository<LabTechnician, Long> {
    LabTechnician findByEmail(String email);
}
