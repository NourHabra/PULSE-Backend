package com.pulse.medicalrecord.controller;

import com.pulse.medicalrecord.dto.MedicalRecordEntryDTO;
import com.pulse.medicalrecord.model.MedicalRecordEntry;
import com.pulse.medicalrecord.service.MedicalRecordEntryService;
import com.pulse.diagnosis.service.DiagnosisService;
import com.pulse.emergencyevent.service.EmergencyEventService;
import com.pulse.prescription.service.PrescriptionService;
import com.pulse.labresult.service.LabResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import com.pulse.security.service.JwtService;
import com.pulse.user.model.Patient;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/mre")
public class MedicalRecordEntryController {

    private final MedicalRecordEntryService mreService;
    private final DiagnosisService           diagnosisService;
    private final EmergencyEventService      emergencyEventService;
    private final PrescriptionService        prescriptionService;
    private final LabResultService           labResultService;
    private final JwtService jwtService;


    public MedicalRecordEntryController(
            MedicalRecordEntryService mreService,
            DiagnosisService           diagnosisService,
            EmergencyEventService      emergencyEventService,
            PrescriptionService        prescriptionService,
            LabResultService           labResultService,
            JwtService jwtService
    ) {
        this.mreService          = mreService;
        this.diagnosisService    = diagnosisService;
        this.emergencyEventService = emergencyEventService;
        this.prescriptionService  = prescriptionService;
        this.labResultService     = labResultService;
        this.jwtService=jwtService;
    }


    @PreAuthorize("@consentGuard.canRead(#patientId)")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalRecordEntryDTO>> listMreWithDetails(
            @PathVariable Long patientId
    ) {
        List<MedicalRecordEntry> mres = mreService.findAllByPatientId(patientId);

        List<MedicalRecordEntryDTO> dtos = mres.stream()
                .map(mre -> {
                    MedicalRecordEntryDTO dto = new MedicalRecordEntryDTO(mre);

                    diagnosisService.findByMreId(mre.getMedicalRecordEntryId())
                            .ifPresent(dto::setDiagnosis);

                    emergencyEventService.findByMreId(mre.getMedicalRecordEntryId())
                            .ifPresent(dto::setEmergencyEvent);

                    prescriptionService.findByMreId(mre.getMedicalRecordEntryId())
                            .ifPresent(dto::setPrescription);

//                    labResultService.findByMreId(mre.getMedicalRecordEntryId())
//                            .ifPresent(dto::setLabResult);

                    return dto;
                })
                .collect(Collectors.toList());


        return ResponseEntity.ok(dtos);
    }


    @GetMapping("/{entryId}")
    public ResponseEntity<MedicalRecordEntryDTO> getMreWithDetails(
            @PathVariable Long entryId
    ) {
        MedicalRecordEntry mre = mreService.findById(entryId);
        if (mre == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "MRE not found: " + entryId);
        }

        MedicalRecordEntryDTO dto = mapToDto(mre);

        return ResponseEntity.ok(dto);
    }
    private MedicalRecordEntryDTO mapToDto(MedicalRecordEntry mre) {
        MedicalRecordEntryDTO dto = new MedicalRecordEntryDTO(mre);

        diagnosisService.findByMreId(mre.getMedicalRecordEntryId())
                .ifPresent(dto::setDiagnosis);

        emergencyEventService.findByMreId(mre.getMedicalRecordEntryId())
                .ifPresent(dto::setEmergencyEvent);

        prescriptionService.findByMreId(mre.getMedicalRecordEntryId())
                .ifPresent(dto::setPrescription);

//        labResultService.findByMreId(mre.getMedicalRecordEntryId())
//                .ifPresent(dto::setLabResult);

        return dto;
    }
    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/patient/me")
    public ResponseEntity<List<MedicalRecordEntryDTO>> listMyMre( @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : authHeader;

        UserDetails userDetails = jwtService.getUserFromToken(token);
        if (!jwtService.isTokenValid(token, userDetails)) {
            throw new RuntimeException("Invalid or expired JWT");
        }
        if (userDetails instanceof Patient patient)
        {
            List<MedicalRecordEntry> mres = mreService.findAllByPatientId(patient.getUserId());

            List<MedicalRecordEntryDTO> dtos = mres.stream()
                    .map(mre -> {
                        MedicalRecordEntryDTO dto = new MedicalRecordEntryDTO(mre);

                        diagnosisService.findByMreId(mre.getMedicalRecordEntryId())
                                .ifPresent(dto::setDiagnosis);

                        emergencyEventService.findByMreId(mre.getMedicalRecordEntryId())
                                .ifPresent(dto::setEmergencyEvent);

                        prescriptionService.findByMreId(mre.getMedicalRecordEntryId())
                                .ifPresent(dto::setPrescription);

//                        labResultService.findByMreId(mre.getMedicalRecordEntryId())
//                                .ifPresent(dto::setLabResult);

                        return dto;
                    })
                    .collect(Collectors.toList());


            return ResponseEntity.ok(dtos);
        }


        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}















//package com.pulse.medicalrecord.controller;
//
//

//
//@RestController
//@RequestMapping("/mre")
//public class MedicalRecordEntryController {
//
//
//    private final MedicalRecordEntryService mreService;
//
//
//    public MedicalRecordEntryController(JwtService jwtService,MedicalRecordEntryService mreService) {
//
//        this.jwtService=jwtService;
//        this.mreService = mreService;
//    }
//
//
//    @PreAuthorize("@consentGuard.canRead(#patientId)")
//    @GetMapping("/patient/{patientId}")
//    public ResponseEntity<List<MedicalRecordEntry>> listMre(@PathVariable Long patientId) {
//        List<MedicalRecordEntry> list = mreService.findAllByPatientId(patientId);
//        return ResponseEntity.ok(list);
//    }
//
//    @PreAuthorize("hasRole('PATIENT')")
//    @GetMapping("/patient/me")
//    public ResponseEntity<List<MedicalRecordEntry>> listMyMre( @RequestHeader("Authorization") String authHeader) {
//
//        String token = authHeader.startsWith("Bearer ")
//                ? authHeader.substring(7)
//                : authHeader;
//
//        UserDetails userDetails = jwtService.getUserFromToken(token);
//        if (!jwtService.isTokenValid(token, userDetails)) {
//            throw new RuntimeException("Invalid or expired JWT");
//        }
//        if (userDetails instanceof Patient patient)
//        {
//            List<MedicalRecordEntry> list = mreService.findAllByPatientId(patient.getUserId());
//            return ResponseEntity.ok(list);
//        }
//
//
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//    }
//
//}
//
