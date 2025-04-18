package com.pulse.user.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;


import java.util.List;


@Entity
@Table(name = "Admin")
@PrimaryKeyJoinColumn(name = "AdminID")
public class Admin extends User {


    @JsonManagedReference
    @OneToMany(mappedBy = "authorizedBy", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = false)
    private List<HealthEmployee> authorizedHealthEmployees;

    public Admin() {
        super.setRole("ADMIN");
    }

    @JsonManagedReference
    @OneToMany(mappedBy = "authorizedBy", fetch = FetchType.EAGER)
    private List<HealthEmployee> approvedEmployees;


    public List<HealthEmployee> getAuthorizedHealthEmployees() {
        return authorizedHealthEmployees;
    }

    public void setAuthorizedHealthEmployees(List<HealthEmployee> authorizedHealthEmployees) {
        this.authorizedHealthEmployees = authorizedHealthEmployees;
    }



}
