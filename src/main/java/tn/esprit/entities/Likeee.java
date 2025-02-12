package tn.esprit.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Likeee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long like_id;

    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne
    private Message message;




}