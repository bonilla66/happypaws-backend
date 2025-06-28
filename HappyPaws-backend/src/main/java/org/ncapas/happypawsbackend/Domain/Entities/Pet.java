package org.ncapas.happypawsbackend.Domain.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ncapas.happypawsbackend.Domain.Audit.Auditable;
import org.ncapas.happypawsbackend.Domain.Enums.Gender;
import org.ncapas.happypawsbackend.Domain.Enums.PetStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Pet")
public class Pet  extends Auditable {

    @Id
    @Column(name = "id_pet", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "weight")
    private int weight;

    @Column(name = "sterilized")
    private boolean sterilized;

    @Column(name = "dewormed")
    private boolean parasiteFree;

    @Column(name = "fully_vaccinated")
    private boolean fullyVaccinated;

    @Column(name = "entry_Date")
    private LocalDate entry_Date;

    @Column(name = "review_Date")
    private LocalDate  review_Date;

    @Column(name = "description")
    private String description;

    @Column(name = "history")
    private String history;

    //@Column(name = "photoURL")
    //private String photoURL;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PetStatus status;

    @ManyToOne
    @JoinColumn(name = "Id_Shelter", nullable = false, foreignKey = @ForeignKey(name = "fk_Shelter_Pet"))
    private Shelter shelter;

    @ManyToOne
    @JoinColumn(name = "id_species", nullable = false, foreignKey = @ForeignKey(name = "fk_Species_Characteristics"))
    private Species species;

    @ManyToOne
    @JoinColumn(name = "id_breed", nullable = true, foreignKey = @ForeignKey(name = "fk_Race_Characteristics"))
    private Breed breed;

    @ManyToOne
    @JoinColumn(name = "id_size", nullable = false, foreignKey = @ForeignKey(name = "fk_Size_Characteristics"))
    private Size size;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false, foreignKey = @ForeignKey(name = "fk_user_pet"))
    private User user;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Aplication> applications;

    @ManyToMany
    @JoinTable(
            name = "pet_attribute_pet",
            joinColumns = @JoinColumn(name = "id_pet"),
            inverseJoinColumns = @JoinColumn(name = "id_pet_attribute")
    )
    private List<Pet_Attribute> attributes;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

}
