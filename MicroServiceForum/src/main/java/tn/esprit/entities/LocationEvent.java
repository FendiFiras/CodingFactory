package tn.esprit.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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



@JsonIgnore
    @OneToMany(cascade= CascadeType.ALL, mappedBy="locationEvent")

private List<Planning> Plannings;
}



