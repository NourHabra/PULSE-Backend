package com.pulse.laboratory.service;

import com.pulse.laboratory.model.Laboratory;
import com.pulse.laboratory.repository.LaboratoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LaboratoryService {

    private final LaboratoryRepository laboratoryRepository;

    public LaboratoryService(LaboratoryRepository laboratoryRepository) {
        this.laboratoryRepository = laboratoryRepository;
    }

    // CREATE
    public Laboratory createLab(Laboratory lab) {
        return laboratoryRepository.save(lab);
    }

    // READ (all)
    public List<Laboratory> getAllLabs() {
        return laboratoryRepository.findAll();
    }

    // READ (by ID)
    public Optional<Laboratory> getLabById(Long id) {
        return laboratoryRepository.findById(id);
    }

    // UPDATE
    public Laboratory updateLab(Long id, Laboratory updatedLab) {
        return laboratoryRepository.findById(id).map(existing -> {
            existing.setName(updatedLab.getName());
            existing.setLicenseNumber(updatedLab.getLicenseNumber());
            existing.setWorkingHours(updatedLab.getWorkingHours());
            existing.setManager(updatedLab.getManager());
            existing.setPhone(updatedLab.getPhone());
            existing.setAddress(updatedLab.getAddress());
            existing.setLocationCoordinates(updatedLab.getLocationCoordinates());
            return laboratoryRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Laboratory not found with ID: " + id));
    }

    // DELETE
    public void deleteLab(Long id) {
        if (!laboratoryRepository.existsById(id)) {
            throw new RuntimeException("Laboratory not found with ID: " + id);
        }
        laboratoryRepository.deleteById(id);
    }
}
