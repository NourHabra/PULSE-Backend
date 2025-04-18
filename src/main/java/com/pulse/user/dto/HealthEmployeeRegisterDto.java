package com.pulse.user.dto;

public class HealthEmployeeRegisterDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;


    private Long authorizedByAdminId;

    public HealthEmployeeRegisterDto() {}

    // Getters & Setters

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public Long getAuthorizedByAdminId() {
        return authorizedByAdminId;
    }
    public void setAuthorizedByAdminId(Long authorizedByAdminId) {
        this.authorizedByAdminId = authorizedByAdminId;
    }
}
