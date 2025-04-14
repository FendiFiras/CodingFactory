package tn.esprit.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(nullable = false)
    private String theme;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private Boolean notificationEnabled;

    @OneToOne(mappedBy = "userPreference")
    @JsonIgnore
    private User user;
}
