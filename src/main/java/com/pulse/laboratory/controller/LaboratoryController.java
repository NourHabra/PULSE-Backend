package com.pulse.laboratory.controller;

import com.pulse.laboratory.model.Laboratory;
import com.pulse.laboratory.service.LaboratoryService;
import com.pulse.user.model.LabTechnician;
import com.pulse.user.repository.LabTechnicianRepository;
import com.pulse.security.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/labs")
public class LaboratoryController {

    private static final Logger logger = LoggerFactory.getLogger(LaboratoryController.class);

    private final LaboratoryService laboratoryService;
    private final JwtService jwtService;
    private final LabTechnicianRepository techRepo;

    public LaboratoryController(
            LaboratoryService laboratoryService,
            JwtService jwtService,
            LabTechnicianRepository techRepo
    ) {
        this.laboratoryService = laboratoryService;
        this.jwtService        = jwtService;
        this.techRepo          = techRepo;
    }


    @PreAuthorize("hasRole('LAB_TECHNICIAN')")
    @PostMapping("/add")
    public ResponseEntity<Laboratory> createLab(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Laboratory labRequest
    ) {

        String token = authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : authHeader;
        UserDetails userDetails = jwtService.getUserFromToken(token);
        if (!jwtService.isTokenValid(token, userDetails)) {
            throw new RuntimeException("Invalid or expired JWT");
        }


        LabTechnician manager = techRepo.findByEmail(userDetails.getUsername());
        if (manager == null) {
            throw new AccessDeniedException("You are not registered as a Lab Technician");
        }

        logger.info("Creating lab: managerId={} lab={}", manager.getUserId(), labRequest);
        Laboratory created = laboratoryService.createLab(labRequest, manager.getUserId());
        logger.info("Lab created: {}", created.getLaboratoryId());
        return ResponseEntity.ok(created);
    }

    @PreAuthorize("hasRole('LAB_TECHNICIAN')")
    @PostMapping("/addtechnician")
    public ResponseEntity<LabTechnician> addTechnicianToLab(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String,Long> payload
    ) {
        String token = authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : authHeader;
        UserDetails userDetails = jwtService.getUserFromToken(token);
        if (!jwtService.isTokenValid(token, userDetails)) {
            throw new RuntimeException("Invalid or expired JWT");
        }
        LabTechnician caller = techRepo.findByEmail(userDetails.getUsername());
        if (caller == null) {
            throw new AccessDeniedException("You are not registered as a Lab Technician");
        }
        if (caller.getWorkingLab() == null) {
            throw new IllegalStateException("Manager has no workingLab assigned yet");
        }
        Long labId = caller.getWorkingLab().getLaboratoryId();

        Long technicianId = payload.get("technicianId");

        String technicianRole = "Technician";

        Laboratory lab = laboratoryService.getLabById(labId)
                .orElseThrow(() -> new IllegalArgumentException("Laboratory not found: " + labId));
        if (lab.getManager() == null
                || !lab.getManager().getUserId().equals(caller.getUserId())) {
            throw new AccessDeniedException("Only the lab manager may add technicians");
        }

        LabTechnician updated = laboratoryService.addTechnicianToLab(
                labId, technicianId, technicianRole
        );

        return ResponseEntity.ok(updated);
    }

//    Old

    // READ all
    @GetMapping
    public ResponseEntity<List<Laboratory>> getAllLabs() {
        return ResponseEntity.ok(laboratoryService.getAllLabs());
    }

    // READ by ID
    @GetMapping("/{id}")
    public ResponseEntity<Laboratory> getLabById(@PathVariable Long id) {
        return laboratoryService.getLabById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Laboratory> updateLab(
            @PathVariable Long id,
            @RequestBody Laboratory updatedLab
    ) {
        return ResponseEntity.ok(laboratoryService.updateLab(id, updatedLab));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLab(@PathVariable Long id) {
        laboratoryService.deleteLab(id);
        return ResponseEntity.noContent().build();
    }
}
