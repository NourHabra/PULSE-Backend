package com.pulse.user.dto;

import java.time.LocalDate;

public class HealthEmployeeRegisterDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private String gender;
    private LocalDate dateOfBirth;
    private String placeOfBirth;
    private String mobileNumber;
    private String address;
    private String pictureUrl;

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


    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }
    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }
    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
