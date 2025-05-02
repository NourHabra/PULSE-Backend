package com.pulse.drug.model;


import jakarta.persistence.*;

@Entity
@Table(name = "drug")
public class Drug {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long drugId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String strength ;

    private String manufacturer;
    private String activeSubstance;
    private Integer amount;
    private String type;

    public Drug() {}
    public Drug(String name, String strength, String manufacturer, String activeSubstance, Integer amount, String type) {
        this.name = name;
        this.strength = strength;
        this.manufacturer = manufacturer;
        this.activeSubstance = activeSubstance;
        this.amount = amount;
        this.type = type;
    }

    public Long getDrugId() { return drugId; }
    public void setDrugId(Long drugId) { this.drugId = drugId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStrength() { return strength; }
    public void setStrength(String strength) { this.strength = strength; }
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public String getActiveSubstance() { return activeSubstance; }
    public void setActiveSubstance(String activeSubstance) { this.activeSubstance = activeSubstance; }
    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
