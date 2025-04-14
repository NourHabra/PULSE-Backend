package com.pulse.user.dto;

import com.pulse.user.model.User;
import java.time.Instant;

public class UserLoginResponse {
    private String message;
    private String token;
    private Instant expirationTime;
    private User user;

    public UserLoginResponse() {}

    public UserLoginResponse(
            String message,
            String token,
            Instant expirationTime,
            User user
    ) {
        this.message = message;
        this.token = token;
        this.expirationTime = expirationTime;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Instant expirationTime) {
        this.expirationTime = expirationTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
