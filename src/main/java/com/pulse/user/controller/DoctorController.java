package com.pulse.user.controller;

import com.pulse.security.service.JwtService;
import com.pulse.user.dto.*;
import com.pulse.user.model.Doctor;
import com.pulse.user.model.Patient;
import com.pulse.user.service.DoctorService;
import com.pulse.user.service.PatientService;
import com.pulse.util.FileStorageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.pulse.email.service.ActivationService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class DoctorController {

    private final DoctorService doctorService;
    private final JwtService jwtService;
    private final ActivationService activationSvc;

    private final PatientService patientService;
    public DoctorController(DoctorService doctorService, JwtService jwtService, ActivationService activationSvc,PatientService patientService) {
        this.doctorService = doctorService;
        this.jwtService = jwtService;
        this.activationSvc = activationSvc;
        this.patientService=patientService;
    }

    @PostMapping("/register/doctor")
    public ResponseEntity<UserLoginResponse> registerDoctor(
            @RequestPart("data") DoctorRegisterDto dto,
            @RequestPart("profilePicture") MultipartFile profilePictureFile
    ) throws IOException {
        String picturePath = FileStorageUtil.saveFile(profilePictureFile, "profile_pictures");
        dto.setPictureUrl(picturePath);
        Doctor doctor = doctorService.register(dto);

        activationSvc.sendActivation(doctor);

        String token = jwtService.generateToken(doctor);

        return ResponseEntity.ok(new UserLoginResponse(
                "Doctor registered successfully",
                token,
                jwtService.getExpirationTime(),
                doctor
        ));
    }



    @PostMapping("/login/doctor")
    public ResponseEntity<UserLoginResponse> loginDoctor(@RequestBody DoctorLoginDto dto) {
        Doctor doctor = doctorService.login(dto);
        String token = jwtService.generateToken(doctor);

        return ResponseEntity.ok(new UserLoginResponse(
                "Doctor login successful",
                token,
                jwtService.getExpirationTime(),
                doctor
        ));
    }

    @GetMapping("/featured-doctors")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<FeaturedDoctorDto>> featuredDoctors() {
        return ResponseEntity.ok(doctorService.getTodayFeaturedDoctorsDto());
    }


    @GetMapping("/doctors/{id}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<DoctorProfileDto> doctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorProfile(id));
    }

    @GetMapping("/{id}/coordinates/embed")
    public ResponseEntity<String> getDoctorCoordinatesEmbedLink(@PathVariable Long id) {
        String embedLink = doctorService.getDoctorCoordinatesEmbedLink(id);
        return ResponseEntity.ok(embedLink);
    }

    @GetMapping("/doctors")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<DoctorProfileDto>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }


    @GetMapping("/profile/doctor")
    public ResponseEntity<UserLoginResponse> getPatientProfile(@RequestHeader("Authorization") String token) {

        String jwttoken = token.startsWith("Bearer ")
                ? token.substring(7)
                : token;
        UserDetails user = jwtService.getUserFromToken(jwttoken);

        if (user != null && user instanceof Doctor) {
            Doctor doc = (Doctor) user;
            String tokenResponse = jwtService.generateToken(doc);


            return ResponseEntity.ok(new UserLoginResponse(
                    "Doctor profile fetched successfully",
                    tokenResponse,
                    jwtService.getExpirationTime(),
                    doc
            ));
        } else {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new UserLoginResponse(
                    "Unauthorized access",
                    null,
                    null,
                    null
            ));
        }
    }




    @PutMapping(value = "/update/doctor", consumes = "multipart/form-data")
    public ResponseEntity<UserLoginResponse> updateDoctor(
            @RequestHeader("Authorization") String token,
            @RequestPart("dto") DoctorUpdateDto dto,
            @RequestPart(value = "profile_picture", required = false) MultipartFile profilePictureFile
    ) throws IOException {
        String jwttoken = token.startsWith("Bearer ") ? token.substring(7) : token;
        UserDetails user = jwtService.getUserFromToken(jwttoken);

        if (user != null && user instanceof Doctor) {
            Doctor doctor = (Doctor) user;

            if (profilePictureFile != null && !profilePictureFile.isEmpty()) {
                String picturePath = FileStorageUtil.saveFile(profilePictureFile, "profile_pictures");
                dto.setPictureUrl(picturePath);
            }

            Doctor updated = doctorService.updateDoctor(doctor, dto);
            String newToken = jwtService.generateToken(updated);

            return ResponseEntity.ok(new UserLoginResponse(
                    "Doctor profile updated successfully",
                    newToken,
                    jwtService.getExpirationTime(),
                    updated
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new UserLoginResponse(
                    "Unauthorized access",
                    null,
                    null,
                    null
            ));
        }
    }


    @GetMapping("/patient/{patientId}")
    @PreAuthorize("@consentGuard.canRead(#patientId)")
    public ResponseEntity<UserLoginResponse> getPatientProfile(@RequestHeader("Authorization") String token,
                                                               @PathVariable Long patientId) {

        String jwttoken = token.startsWith("Bearer ") ? token.substring(7) : token;
        UserDetails doctor = jwtService.getUserFromToken(jwttoken);

        if (doctor != null && doctor instanceof Doctor) {
            Patient patient = patientService.getPatientById(patientId);

            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UserLoginResponse(
                        "Patient not found",
                        null,
                        null,
                        null
                ));
            }


            String tokenResponse = jwtService.generateToken(patient);

            return ResponseEntity.ok(new UserLoginResponse(
                    "Patient profile fetched successfully",
                    tokenResponse,
                    jwtService.getExpirationTime(),
                    patient
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new UserLoginResponse(
                    "Unauthorized access",
                    null,
                    null,
                    null
            ));
        }
    }

    @GetMapping("/patients/recent")
    public ResponseEntity<?> getRecentPatients(
            @RequestHeader("Authorization") String token
    ) {
        String jwt = token.startsWith("Bearer ")
                ? token.substring(7)
                : token;

        UserDetails user = jwtService.getUserFromToken(jwt);
        if (!(user instanceof Doctor)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long doctorId = ((Doctor) user).getUserId();
        List<PatientSummaryDto> last5 = doctorService
                .getLastDiagnosedPatients(doctorId, 5);

        if (last5.isEmpty()) {
            return ResponseEntity
                    .ok("No patients found for this doctor yet.");
        }

        return ResponseEntity.ok(last5);
    }
    @GetMapping("/diagnoses/count/today")
    public ResponseEntity<Long> countToday(@RequestHeader("Authorization") String token) {
        Long docId = extractDoctorId(token);
        if (docId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        long count = doctorService.countDiagnosesToday(docId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/diagnoses/count/week")
    public ResponseEntity<Long> countWeek(@RequestHeader("Authorization") String token) {
        Long docId = extractDoctorId(token);
        if (docId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        long count = doctorService.countDiagnosesThisWeek(docId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/diagnoses/count/month")
    public ResponseEntity<Long> countMonth(@RequestHeader("Authorization") String token) {
        Long docId = extractDoctorId(token);
        if (docId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        long count = doctorService.countDiagnosesThisMonth(docId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/diagnoses/count/year")
    public ResponseEntity<Long> countYear(@RequestHeader("Authorization") String token) {
        Long docId = extractDoctorId(token);
        if (docId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        long count = doctorService.countDiagnosesThisYear(docId);
        return ResponseEntity.ok(count);
    }

    private Long extractDoctorId(String bearerToken) {
        String jwt = bearerToken.startsWith("Bearer ")
                ? bearerToken.substring(7)
                : bearerToken;
        UserDetails user = jwtService.getUserFromToken(jwt);
        if (user instanceof Doctor) {
            return ((Doctor) user).getUserId();
        }
        return null;
    }
}
