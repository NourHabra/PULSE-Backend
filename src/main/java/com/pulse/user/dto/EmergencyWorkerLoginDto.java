package com.pulse.user.dto;

public class EmergencyWorkerLoginDto {

    private String email;
    private String password;

    public EmergencyWorkerLoginDto() {}

    // Getters & Setters
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
