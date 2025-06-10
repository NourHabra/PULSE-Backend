package com.pulse.user.model;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@PrimaryKeyJoinColumn(name = "PatientID")
public class Patient extends User {

    @Column(name = "Height")
    private Double height;

    @Column(name = "Weight")
    private Double weight;

    @Column(name = "BloodType")
    private String bloodType;


    @Column(name = "IDImage")
    private String idImage;

    public Patient() {
        super.setRole("patient");
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }



    public String getIdImage() {
        return idImage;
    }

    public void setIdImage(String idImage) {
        this.idImage = idImage;
    }

}
