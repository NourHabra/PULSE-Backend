package com.pulse.pharmacy.service;

import com.pulse.pharmacy.model.Pharmacy;
import com.pulse.pharmacy.repository.PharmacyRepository;
import com.pulse.user.model.Doctor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PharmacyService {

    private final PharmacyRepository pharmacyRepository;

    public PharmacyService(PharmacyRepository pharmacyRepository) {
        this.pharmacyRepository = pharmacyRepository;
    }

    // CREATE
    public Pharmacy createPharmacy(Pharmacy pharmacy) {
        return pharmacyRepository.save(pharmacy);
    }

    // READ ALL
    public List<Pharmacy> getAllPharmacies() {
        return pharmacyRepository.findAll();
    }

    // READ ONE
    public Optional<Pharmacy> getPharmacyById(Long id) {
        return pharmacyRepository.findById(id);
    }

    // UPDATE
    public Pharmacy updatePharmacy(Long id, Pharmacy updatedPharmacy) {
        return pharmacyRepository.findById(id).map(existing -> {
            existing.setName(updatedPharmacy.getName());
            existing.setLicenseNumber(updatedPharmacy.getLicenseNumber());
            existing.setWorkingHours(updatedPharmacy.getWorkingHours());
            existing.setManager(updatedPharmacy.getManager());
            existing.setPhone(updatedPharmacy.getPhone());
            existing.setAddress(updatedPharmacy.getAddress());
            existing.setLocationCoordinates(updatedPharmacy.getLocationCoordinates());
            return pharmacyRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Pharmacy not found with ID: " + id));
    }

    // DELETE
    public void deletePharmacy(Long id) {
        if (!pharmacyRepository.existsById(id)) {
            throw new RuntimeException("Pharmacy not found with ID: " + id);
        }
        pharmacyRepository.deleteById(id);
    }

    public String convertToEmbedLink(String coordinatesLink) {
        if (coordinatesLink == null || !coordinatesLink.startsWith("https://www.google.com/maps/place/")) {
            throw new IllegalArgumentException("Invalid coordinates link format");
        }

        String coords = coordinatesLink.replace("https://www.google.com/maps/place/", "");

        return "https://www.google.com/maps?q=" + coords + "&z=15&output=embed";
    }
    public String getPharmacyCoordinatesEmbedLink(Long id) {
        Pharmacy pharm = pharmacyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + id));
        String coordinatesLink = pharm.getLocationCoordinates();

        return convertToEmbedLink(coordinatesLink);
    }
}
