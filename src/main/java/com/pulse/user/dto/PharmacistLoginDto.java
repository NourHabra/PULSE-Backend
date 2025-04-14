package com.pulse.user.dto;

public class PharmacistLoginDto {

    private String email;
    private String password;
    // If extra pharmacist-specific login requirements exist, add them here

    public PharmacistLoginDto() {}

    // Getters & setters
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
