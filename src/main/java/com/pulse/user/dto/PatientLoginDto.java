package com.pulse.user.dto;

public class PatientLoginDto {

    private String email;
    private String password;
    // If you need extra fields for patient login, add them here.

    public PatientLoginDto() {
    }

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
