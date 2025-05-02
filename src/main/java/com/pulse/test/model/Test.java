package com.pulse.test.model;


import jakarta.persistence.*;

@Entity
@Table(name = "test")
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testId;

    @Column(nullable = false)
    private String name;

    private String type;

    @Column(length = 2000)
    private String description;

    private String normalValues;

    public Test() {}

    public Test(Long testId, String name, String type, String description, String normalValues) {
        this.testId = testId;
        this.name = name;
        this.type = type;
        this.description = description;
        this.normalValues = normalValues;
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNormalValues() {
        return normalValues;
    }

    public void setNormalValues(String normalValues) {
        this.normalValues = normalValues;
    }
}