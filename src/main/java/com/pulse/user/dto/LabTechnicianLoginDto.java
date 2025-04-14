package com.pulse.user.dto;

public class LabTechnicianLoginDto {

    private String email;
    private String password;

    public LabTechnicianLoginDto() {}

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
