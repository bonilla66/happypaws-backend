package org.ncapas.happypawsbackend.Domain.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.ncapas.happypawsbackend.Domain.Audit.Auditable;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Setter
@Getter

@Table(name = "Image")
public class Image extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank
    private String name;

    @NotBlank
    private String imgURL;

    @NotBlank
    private String imageId;

}
