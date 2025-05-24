package com.pulse.consent.security;

import com.pulse.consent.model.ConsentStatus;
import com.pulse.consent.repository.ConsentRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Component("consentGuard")
public class ConsentGuard {

    private final ConsentRepository repo;

    public ConsentGuard(ConsentRepository repo) {
        this.repo = repo;
    }


    public boolean canRead(Long requestedPatientId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw denied();
        }

        Long callerId = ((com.pulse.user.model.User) auth.getPrincipal()).getUserId();
        boolean isPatient = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"));
        boolean isDoctor  = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"));


        if (isPatient) {
            if (callerId.equals(requestedPatientId)) {
                return true;
            }
            throw denied();
        }


        if (isDoctor) {
            boolean allowed = repo.existsByPatientIdAndDoctorIdAndStatus(
                    requestedPatientId,
                    callerId,
                    ConsentStatus.ACTIVE);
            if (allowed) return true;
            throw denied();
        }


        throw denied();
    }

    private ResponseStatusException denied() {
        throw new ResponseStatusException(
                FORBIDDEN,
                "You are not authorized to see this data"
        );
    }
}
