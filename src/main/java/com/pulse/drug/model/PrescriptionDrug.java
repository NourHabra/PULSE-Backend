package com.pulse.drug.model;


import com.pulse.drug.model.Drug;
import com.pulse.prescription.model.Prescription;
import jakarta.persistence.*;


import com.pulse.medicalrecord.model.MedicalRecordEntry;
import jakarta.persistence.*;

@Entity
@Table(name = "prescription_drug")
public class PrescriptionDrug {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "prescription_id")
    private Prescription prescription;

    @ManyToOne(optional = false)
    @JoinColumn(name = "drug_id")
    private Drug drug;

    private String dosage;
    private String duration;
    private String notes;

    public PrescriptionDrug() {}

    public PrescriptionDrug(Prescription prescription, Drug drug, String dosage, String duration, String notes) {
        this.prescription = prescription;
        this.drug = drug;
        this.dosage = dosage;
        this.duration = duration;
        this.notes = notes;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Prescription getPrescription() { return prescription; }
    public void setPrescription(Prescription prescription) { this.prescription = prescription; }
    public Drug getDrug() { return drug; }
    public void setDrug(Drug drug) { this.drug = drug; }
    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}