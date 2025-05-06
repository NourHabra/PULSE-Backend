package com.pulse.user.dto;

import com.pulse.user.model.Doctor;
import java.time.LocalDate;

public class FeaturedDoctorDto {

    private Long id;
    private String fullName;
    private String pictureUrl;



    public Long getId()              { return id; }
    public void setId(Long id)       { this.id = id; }

    public String getFullName()      { return fullName; }
    public void setFullName(String n){ this.fullName = n; }

    public String getPictureUrl()    { return pictureUrl; }
    public void setPictureUrl(String u){ this.pictureUrl = u; }



    public static FeaturedDoctorDto fromEntity(Doctor d) {
        FeaturedDoctorDto dto = new FeaturedDoctorDto();
        dto.setId(d.getUserId());
        dto.setFullName(d.getFirstName() + " " + d.getLastName());
        dto.setPictureUrl(d.getPictureUrl());
        return dto;
    }
}
