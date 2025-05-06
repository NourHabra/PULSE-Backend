package com.pulse.user.dto;

import com.pulse.user.model.Doctor;
import java.time.LocalDate;

public class FeaturedDoctorDto {

    private Long id;
    private String fullName;
    private String pictureUrl;
    private String specialization;

    private String coordinates;
    public Long getId()              { return id; }
    public void setId(Long id)       { this.id = id; }

    public String getFullName()      { return fullName; }
    public void setFullName(String n){ this.fullName = n; }

    public String getPictureUrl()    { return pictureUrl; }
    public void setPictureUrl(String u){ this.pictureUrl = u; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getCoordinates()          { return coordinates; }
    public void setCoordinates(String c)    { this.coordinates = c; }

    public static FeaturedDoctorDto fromEntity(Doctor d) {
        FeaturedDoctorDto dto = new FeaturedDoctorDto();
        dto.setId(d.getUserId());
        dto.setFullName(d.getFirstName() + " " + d.getLastName());
        dto.setPictureUrl(d.getPictureUrl());
        dto.setSpecialization(d.getSpecialization());
        dto.setCoordinates(d.getCoordinates());
        return dto;
    }
}
