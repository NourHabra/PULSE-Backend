package com.pulse.user.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "HealthEmployee")
@PrimaryKeyJoinColumn(name = "HealthEmployeeID")
public class HealthEmployee extends User {

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "AuthorizedBy")
    private Admin authorizedBy;

    public HealthEmployee() {
        super.setRole("Health Employee");
    }

    public Admin getAuthorizedBy() {
        return authorizedBy;
    }

    public void setAuthorizedBy(Admin authorizedBy) {
        this.authorizedBy = authorizedBy;
    }
}
