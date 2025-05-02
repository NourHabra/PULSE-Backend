package com.pulse.vital.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vital")
public class Vital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vitalId;

    private String name;
    private String description;
    private String normalValue;
}
