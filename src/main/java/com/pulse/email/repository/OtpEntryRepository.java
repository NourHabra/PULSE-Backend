package com.pulse.email.repository;

import com.pulse.email.model.OtpEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OtpEntryRepository extends JpaRepository<OtpEntry, Long> {
    void deleteByEmail(String email);
    Optional<OtpEntry> findTopByEmailOrderByExpiryDesc(String email);
    Optional<OtpEntry> findByEmail(String email);



}