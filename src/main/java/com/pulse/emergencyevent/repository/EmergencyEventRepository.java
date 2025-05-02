package com.pulse.emergencyevent.repository;

import com.pulse.emergencyevent.model.EmergencyEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmergencyEventRepository extends JpaRepository<EmergencyEvent, Long> {
    List<EmergencyEvent> findByMedicalRecordEntry_Patient_UserId(Long userId);
}