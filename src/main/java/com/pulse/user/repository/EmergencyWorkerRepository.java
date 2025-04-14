package com.pulse.user.repository;

import com.pulse.user.model.EmergencyWorker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmergencyWorkerRepository extends JpaRepository<EmergencyWorker, Long> {
    EmergencyWorker findByEmail(String email);
}
