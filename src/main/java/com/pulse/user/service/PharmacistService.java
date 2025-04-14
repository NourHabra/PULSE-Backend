package com.pulse.user.service;

import com.pulse.exception.EmailAlreadyExistsException;
import com.pulse.pharmacy.model.Pharmacy;
import com.pulse.pharmacy.repository.PharmacyRepository;
import com.pulse.user.dto.PharmacistLoginDto;
import com.pulse.user.dto.PharmacistRegisterDto;
import com.pulse.user.model.Pharmacist;
import com.pulse.user.repository.PharmacistRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PharmacistService {

    private final PharmacistRepository pharmacistRepository;
    private final PharmacyRepository pharmacyRepository;
    private final PasswordEncoder passwordEncoder;

    public PharmacistService(
            PharmacistRepository pharmacistRepository,
            PharmacyRepository pharmacyRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.pharmacistRepository = pharmacistRepository;
        this.pharmacyRepository = pharmacyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Pharmacist register(PharmacistRegisterDto dto) {
        if (pharmacistRepository.findByEmail(dto.getEmail()) != null) {
            throw new EmailAlreadyExistsException(dto.getEmail());
        }

        Pharmacy pharmacy = pharmacyRepository.findById(dto.getPharmacyId())
                .orElseThrow(() -> new RuntimeException("Pharmacy not found"));

        Pharmacist pharmacist = new Pharmacist();
        pharmacist.setFirstName(dto.getFirstName());
        pharmacist.setLastName(dto.getLastName());
        pharmacist.setEmail(dto.getEmail());
        pharmacist.setPassword(passwordEncoder.encode(dto.getPassword()));
        pharmacist.setRole("PHARMACIST");
        pharmacist.setLicenseNumber(dto.getLicenseNumber());
        pharmacist.setPharmacistRole(dto.getPharmacistRole());
        pharmacist.setWorkingPharmacy(pharmacy);

        return pharmacistRepository.save(pharmacist);
    }

    public Pharmacist login(PharmacistLoginDto dto) {
        Pharmacist pharmacist = pharmacistRepository.findByEmail(dto.getEmail());
        if (pharmacist == null || !passwordEncoder.matches(dto.getPassword(), pharmacist.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return pharmacist;
    }
}
