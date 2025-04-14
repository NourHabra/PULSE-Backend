package com.pulse.user.dto;

public class DoctorLoginDto {

    private String email;
    private String password;

    // If you want extra fields for doctor login, add them here.
    // For example, a 2FA code or something else.

    public DoctorLoginDto() {
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
