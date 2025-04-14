package com.pulse.user.repository;

import com.pulse.user.model.Pharmacist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmacistRepository extends JpaRepository<Pharmacist, Long> {
    Pharmacist findByEmail(String email);
}
