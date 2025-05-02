package com.pulse.test.model;


import com.pulse.laboratory.model.Laboratory;
import com.pulse.test.model.Test;
import jakarta.persistence.*;

@Entity
@Table(name = "laboratory_test")
public class LaboratoryTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lab_id", referencedColumnName = "laboratoryId")
    private Laboratory laboratory;

    @ManyToOne(optional = false)
    @JoinColumn(name = "test_id", referencedColumnName = "testId")
    private Test test;

    @Column(nullable = false)
    private Double price;

    public LaboratoryTest() {}

    public LaboratoryTest(Laboratory laboratory, Test test, Double price) {
        this.laboratory = laboratory;
        this.test = test;
        this.price = price;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Laboratory getLaboratory() { return laboratory; }
    public void setLaboratory(Laboratory laboratory) { this.laboratory = laboratory; }
    public Test getTest() { return test; }
    public void setTest(Test test) { this.test = test; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
