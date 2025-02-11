package tn.esprit.entities;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPreference;

    private String theme;
    private String language;
    private Boolean notificationEnabled;
    @OneToOne(mappedBy = "userPreference")
    private User user;
}
