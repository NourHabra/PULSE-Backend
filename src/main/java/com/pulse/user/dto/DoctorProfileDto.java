package com.pulse.user.dto;

import com.pulse.user.model.Doctor;
import java.time.LocalDate;

public class DoctorProfileDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String specialization;
    private String licenseNumber;
    private String workingHours;
    private String biography;
    private String gender;
    private LocalDate dateOfBirth;
    private String placeOfBirth;
    private String mobileNumber;
    private String address;
    private String pictureUrl;
    private String coordinates;

    public DoctorProfileDto() { }


    public DoctorProfileDto(Long id,
                            String firstName,
                            String lastName,
                            String email,
                            String specialization,
                            String licenseNumber,
                            String workingHours,
                            String biography,
                            String gender,
                            LocalDate dateOfBirth,
                            String placeOfBirth,
                            String mobileNumber,
                            String address,
                            String pictureUrl,
                            String coordinates) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.specialization = specialization;
        this.licenseNumber = licenseNumber;
        this.workingHours = workingHours;
        this.biography = biography;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.placeOfBirth = placeOfBirth;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.pictureUrl = pictureUrl;
        this.coordinates = coordinates;
    }



    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public String getWorkingHours() { return workingHours; }
    public void setWorkingHours(String workingHours) { this.workingHours = workingHours; }

    public String getBiography() { return biography; }
    public void setBiography(String biography) { this.biography = biography; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getPlaceOfBirth() { return placeOfBirth; }
    public void setPlaceOfBirth(String placeOfBirth) { this.placeOfBirth = placeOfBirth; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPictureUrl() { return pictureUrl; }
    public void setPictureUrl(String pictureUrl) { this.pictureUrl = pictureUrl; }

    public String getCoordinates() { return coordinates; }
    public void setCoordinates(String coordinates) { this.coordinates = coordinates; }

    public static DoctorProfileDto fromEntity(Doctor d) {
        DoctorProfileDto dto = new DoctorProfileDto();
        dto.setId(d.getUserId());
        dto.setFirstName(d.getFirstName());
        dto.setLastName(d.getLastName());
        dto.setEmail(d.getEmail());
        dto.setSpecialization(d.getSpecialization());
        dto.setLicenseNumber(d.getLicenseNumber());
        dto.setWorkingHours(d.getWorkingHours());
        dto.setBiography(d.getBiography());
        dto.setGender(d.getGender());
        dto.setDateOfBirth(d.getDateOfBirth());
        dto.setPlaceOfBirth(d.getPlaceOfBirth());
        dto.setMobileNumber(d.getMobileNumber());
        dto.setAddress(d.getAddress());
        dto.setPictureUrl(d.getPictureUrl());
        dto.setCoordinates(d.getCoordinates());
        return dto;
    }
}
