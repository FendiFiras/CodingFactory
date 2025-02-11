package tn.esprit.entities;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LocationEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLocal;

    private String locationName;

    private String address;

    private float latitude;

    private float longitude;

    @Enumerated(EnumType.STRING)
    private LocationType locationType;

    private String virtualLink;

    @OneToMany(cascade= CascadeType.ALL, mappedBy="locationEvent")
    private Set<Planning> Plannings;
}



