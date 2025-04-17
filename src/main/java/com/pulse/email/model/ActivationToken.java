package com.pulse.email.model;

import jakarta.persistence.Entity;
import com.pulse.user.model.User;
import java.time.Instant;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.OneToOne;
import jakarta.persistence.GenerationType;


@Entity
public class ActivationToken {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Instant expiry;
    @OneToOne
    private User user;

    public ActivationToken() {
        // no-arg constructor required by JPA
    }
    public ActivationToken(Long id, String token, Instant expiry, User user) {
        this.id = id;
        this.token = token;
        this.expiry = expiry;
        this.user = user;
    }

    public Instant getExpiry() {
        return expiry;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }


}