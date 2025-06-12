package com.pulse.user.dto;

public class LabTechnicianUpdateDto {

    private String firstName;
    private String lastName;
    private String gender;
    private String address;
    private String mobileNumber;
    private String pictureUrl;
    private String licenseNumber;
    private Long workingLabId;
    private String technicianRole;





    public Long getWorkingLabId() {
        return workingLabId;
    }

    public void setWorkingLabId(Long workingLabId) {
        this.workingLabId = workingLabId;
    }

    public String getTechnicianRole() {
        return technicianRole;
    }

    public void setTechnicianRole(String technicianRole) {
        this.technicianRole = technicianRole;
    }

    // Getters and Setters
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
