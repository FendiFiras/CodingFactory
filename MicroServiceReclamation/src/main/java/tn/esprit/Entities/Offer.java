package tn.esprit.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Offer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOffer;
    private String title;
    private String description;
    private String requiredSkill;
    private String Location;
    private String duration;
    private int appnmbr;
    private String EmploymentType;
    private String JobResponsibilities;
    private String WhatWeOffer;

    @OneToOne
    private Assignment assignment;



    @OneToMany(cascade=CascadeType.ALL)
    private Set<Application> applicationss;

    @ManyToOne
    private Partnership partnership; // Reference to Partnership instead of company name


}
