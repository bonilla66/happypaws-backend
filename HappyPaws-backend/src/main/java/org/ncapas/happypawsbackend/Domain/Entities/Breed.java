package org.ncapas.happypawsbackend.Domain.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ncapas.happypawsbackend.Domain.Audit.Auditable;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Breed")

public class Breed  extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_breed")
    private Integer id_breed;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "id_species", nullable = false, foreignKey = @ForeignKey(name = "fk_species_breed"))
    private Species species;

    @JsonIgnore
    @OneToMany(mappedBy = "breed")
    private List<Pet> pets;
}
