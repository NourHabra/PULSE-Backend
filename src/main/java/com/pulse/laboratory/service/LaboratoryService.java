package com.pulse.laboratory.service;

import com.pulse.laboratory.model.Laboratory;
import com.pulse.laboratory.repository.LaboratoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.pulse.user.repository.LabTechnicianRepository;
import com.pulse.user.model.LabTechnician;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class LaboratoryService {
    private static final Logger logger = LoggerFactory.getLogger(LaboratoryService.class);
    private final LaboratoryRepository laboratoryRepository;
    private final LabTechnicianRepository technicianRepository;
    private static final String MAPS_PREFIX = "https://www.google.com/maps/place/";




    public LaboratoryService(LaboratoryRepository laboratoryRepository,
                             LabTechnicianRepository technicianRepository) {
        this.laboratoryRepository = laboratoryRepository;
        this.technicianRepository = technicianRepository;
    }


    @Transactional
    public Laboratory createLab(Laboratory lab, Long technicianId) {
        LabTechnician manager = technicianRepository.findById(technicianId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Technician not found with ID: " + technicianId));


        lab.setManager(manager);
        lab.setLocationCoordinates(asMapsLink(lab.getLocationCoordinates()));
        Laboratory savedLab = laboratoryRepository.save(lab);

        manager.setWorkingLab(savedLab);
        manager.setTechnicianRole("Manager");
        technicianRepository.save(manager);

        return savedLab;
    }


    @Transactional
    public LabTechnician addTechnicianToLab(
            Long labId, Long technicianId, String role
    ) {
        Laboratory lab = laboratoryRepository.findById(labId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Laboratory not found with ID: " + labId));

        LabTechnician tech = technicianRepository.findById(technicianId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Technician not found with ID: " + technicianId));

        tech.setWorkingLab(lab);
        tech.setTechnicianRole(role != null ? role : "Technician");
        LabTechnician updatedTech = technicianRepository.save(tech);
        return updatedTech;
    }


//    Old
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
            existing.setLocationCoordinates(asMapsLink(updatedLab.getLocationCoordinates()));
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

    private String asMapsLink(String coords) {
        if (coords == null) return null;
        coords = coords.trim();
        return coords.startsWith(MAPS_PREFIX) ? coords : MAPS_PREFIX + coords;
    }




    public String convertToEmbedLink(String coordinatesLink) {
        if (coordinatesLink == null || !coordinatesLink.startsWith("https://www.google.com/maps/place/")) {
            throw new IllegalArgumentException("Invalid coordinates link format");
        }

        String coords = coordinatesLink.replace("https://www.google.com/maps/place/", "");

        return "https://www.google.com/maps?q=" + coords + "&z=15&output=embed";
    }

    public String getLaboratoryCoordinatesEmbedLink(Long id) {

        Laboratory laboratory = laboratoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Laboratory not found with ID: " + id));
        String coordinatesLink = laboratory.getLocationCoordinates();

        return convertToEmbedLink(coordinatesLink);
    }
}
