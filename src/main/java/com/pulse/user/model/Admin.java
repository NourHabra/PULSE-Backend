package com.pulse.user.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;


import java.util.List;


@Entity
@Table(name = "Admin")
@PrimaryKeyJoinColumn(name = "AdminID")
public class Admin extends User {

    @OneToMany(mappedBy = "authorizedBy", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<HealthEmployee> authorizedHealthEmployees;

    public Admin() {
        super.setRole("ADMIN");
    }

    @JsonManagedReference
    @OneToMany(mappedBy = "authorizedBy")
    private List<HealthEmployee> approvedEmployees;


    public void setAuthorizedHealthEmployees(List<HealthEmployee> authorizedHealthEmployees) {
        this.authorizedHealthEmployees = authorizedHealthEmployees;
    }
}
