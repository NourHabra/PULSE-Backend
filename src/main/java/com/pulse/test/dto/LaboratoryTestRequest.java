package com.pulse.test.dto;


public class LaboratoryTestRequest {
    private Long labId;
    private Long testId;
    private Double price;

    public LaboratoryTestRequest() {}
    public LaboratoryTestRequest(Long labId, Long testId, Double price) {
        this.labId = labId; this.testId = testId; this.price = price;
    }
    public Long getLabId() { return labId; }
    public void setLabId(Long labId) { this.labId = labId; }
    public Long getTestId() { return testId; }
    public void setTestId(Long testId) { this.testId = testId; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
