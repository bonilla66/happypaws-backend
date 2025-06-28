package org.ncapas.happypawsbackend.Domain.Entities;
import org.ncapas.happypawsbackend.Domain.Audit.Auditable;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ncapas.happypawsbackend.Domain.Enums.ApplicationState;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Aplication")

public class Aplication extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_aplication")
    private UUID id_aplication;

    @Column(name = "aplication_Date")
    private Date aplication_Date;

    @Column(name = "Completion_Date")
    private Date Completion_Date;

    @Column(name = "other_Pets")
    private boolean other_Pets;

    @Column(name = "reason_adoption")
    private String reason_adoption;

    @Column(name = "enough_space")
    private boolean enough_space;

    @Column(name = "enought_time")
    private boolean enough_time;

    @Column(name = "location_description")
    private String locationDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "applicaion_state")
    private ApplicationState applicationState;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false, foreignKey = @ForeignKey(name = "fk_Users_Aplication"))
    private User users;

    @ManyToOne
    @JoinColumn(name = "id_pet", nullable = false, foreignKey = @ForeignKey(name = "fk_Pet_Aplication"))
    private Pet pet;

}
