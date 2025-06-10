package com.pulse.user.dto;

public class PatientSummaryDto {

    private Long id;
    private String name;
    private String profilePic;

    public PatientSummaryDto(Long id, String name, String profilePic) {
        this.id = id;
        this.name = name;
        this.profilePic = profilePic;
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
}
