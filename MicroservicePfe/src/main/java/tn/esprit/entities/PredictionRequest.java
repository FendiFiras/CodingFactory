package tn.esprit.entities;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class PredictionRequest {
    private int level;
    private double universityGPA;
    private int fieldOfStudy;
    private int internshipsCompleted;
    private int projectsCompleted;
    private int certifications;
    private double softSkillsScore;
}
