package com.pulse.user.dto;

import java.time.LocalDate;

public class PatientSummaryDto2 {

    private Long id;
    private String name;
    private String profilePic;
    private String address;
    private LocalDate dateOfBirth;

    public PatientSummaryDto2(Long id, String name, String profilePic, String address, LocalDate dateOfBirth) {
        this.id = id;
        this.name = name;
        this.profilePic = profilePic;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
