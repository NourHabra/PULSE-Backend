package com.pulse.user.repository;

import com.pulse.user.model.HealthEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthEmployeeRepository extends JpaRepository<HealthEmployee, Long> {
    HealthEmployee findByEmail(String email);
}
