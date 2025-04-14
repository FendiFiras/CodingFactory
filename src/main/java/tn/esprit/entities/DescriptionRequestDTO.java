package tn.esprit.entities;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DescriptionRequestDTO {
    @NotBlank(message = "Title is mandatory")
    private String title;
    @NotBlank(message = "At least one skill is required")
    private String requiredSkill;
    @NotBlank(message = "Skills are required")

    private String duration;
    @NotBlank(message = "Skills are required")

    private String location;
    @NotBlank(message = "Skills are required")

    private String employmentType;
    @NotBlank(message = "Responsibilities are required")
    private String JobResponsibilities;
    // getters and setters
}