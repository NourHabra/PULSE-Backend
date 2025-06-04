package com.pulse.user.service;

import com.pulse.user.dto.LoginRequest;
import com.pulse.user.model.User;
import com.pulse.user.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class UserService {

    private final AdminRepository adminRepo;
    private final DoctorRepository doctorRepo;
    private final PatientRepository patientRepo;
    private final PharmacistRepository pharmacistRepo;
    private final EmergencyWorkerRepository emergencyWorkerRepo;
    private final LabTechnicianRepository labTechnicianRepo;
    private final HealthEmployeeRepository healthEmployeeRepo;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



    public UserService(AdminRepository adminRepo,
                       DoctorRepository doctorRepo,
                       PatientRepository patientRepo,
                       PharmacistRepository pharmacistRepo,
                       EmergencyWorkerRepository emergencyWorkerRepo,
                       LabTechnicianRepository labTechnicianRepo,
                       HealthEmployeeRepository healthEmployeeRepo,
                       UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.adminRepo = adminRepo;
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
        this.pharmacistRepo = pharmacistRepo;
        this.emergencyWorkerRepo = emergencyWorkerRepo;
        this.labTechnicianRepo = labTechnicianRepo;
        this.healthEmployeeRepo = healthEmployeeRepo;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByEmail(String email) {
        return Stream.of(
                        Optional.ofNullable(adminRepo.findByEmail(email)).map(admin -> (User) admin),
                        Optional.ofNullable(doctorRepo.findByEmail(email)).map(doctor -> (User) doctor),
                        Optional.ofNullable(patientRepo.findByEmail(email)).map(patient -> (User) patient),
                        Optional.ofNullable(pharmacistRepo.findByEmail(email)).map(pharmacist -> (User) pharmacist),
                        Optional.ofNullable(emergencyWorkerRepo.findByEmail(email)).map(ew -> (User) ew),
                        Optional.ofNullable(labTechnicianRepo.findByEmail(email)).map(lt -> (User) lt),
                        Optional.ofNullable(healthEmployeeRepo.findByEmail(email)).map(he -> (User) he)
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }


    public Optional<User> findById(Long id) {
        return Stream.of(
                        Optional.ofNullable(adminRepo.findById(id).orElse(null)).map(admin -> (User) admin),
                        Optional.ofNullable(doctorRepo.findById(id).orElse(null)).map(doctor -> (User) doctor),
                        Optional.ofNullable(patientRepo.findById(id).orElse(null)).map(patient -> (User) patient),
                        Optional.ofNullable(pharmacistRepo.findById(id).orElse(null)).map(pharmacist -> (User) pharmacist),
                        Optional.ofNullable(emergencyWorkerRepo.findById(id).orElse(null)).map(ew -> (User) ew),
                        Optional.ofNullable(labTechnicianRepo.findById(id).orElse(null)).map(lt -> (User) lt),
                        Optional.ofNullable(healthEmployeeRepo.findById(id).orElse(null)).map(he -> (User) he)
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }
    public User login(LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail());
        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return user;
    }
}
