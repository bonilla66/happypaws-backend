package org.ncapas.happypawsbackend.Domain.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ncapas.happypawsbackend.Domain.Audit.Auditable;
import org.ncapas.happypawsbackend.Domain.Enums.PetSize;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Size")

public class Size extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_size")
    private Integer id_size;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", unique = true, nullable = false)
    private PetSize name;

}
