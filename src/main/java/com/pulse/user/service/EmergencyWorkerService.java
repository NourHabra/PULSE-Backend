package com.pulse.user.service;

import com.pulse.user.dto.EmergencyWorkerLoginDto;
import com.pulse.user.dto.EmergencyWorkerRegisterDto;
import com.pulse.user.model.EmergencyWorker;
import com.pulse.user.repository.EmergencyWorkerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.pulse.exception.EmailAlreadyExistsException;

@Service
public class EmergencyWorkerService {

    private final EmergencyWorkerRepository emergencyWorkerRepository;
    private final PasswordEncoder passwordEncoder;

    public EmergencyWorkerService(EmergencyWorkerRepository emergencyWorkerRepository,
                                  PasswordEncoder passwordEncoder) {
        this.emergencyWorkerRepository = emergencyWorkerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public EmergencyWorker register(EmergencyWorkerRegisterDto dto) {
        if (emergencyWorkerRepository.findByEmail(dto.getEmail()) != null) {
            throw new EmailAlreadyExistsException(dto.getEmail());
        }

        EmergencyWorker worker = new EmergencyWorker();
        worker.setFirstName(dto.getFirstName());
        worker.setLastName(dto.getLastName());
        worker.setEmail(dto.getEmail());
        worker.setPassword(passwordEncoder.encode(dto.getPassword()));
        worker.setRole("EMERGENCY_WORKER");

        worker.setLicenseNumber(dto.getLicenseNumber());

        return emergencyWorkerRepository.save(worker);
    }

    public EmergencyWorker login(EmergencyWorkerLoginDto dto) {
        EmergencyWorker worker = emergencyWorkerRepository.findByEmail(dto.getEmail());
        if (worker == null || !passwordEncoder.matches(dto.getPassword(), worker.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return worker;
    }
}
