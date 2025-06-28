package org.ncapas.happypawsbackend.Domain.Audit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.ncapas.happypawsbackend.Domain.Entities.User;

import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public abstract class Auditable {

    @ManyToOne
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_created_by_user"))
    private User createdBy;

    @CreationTimestamp
    @Column(name = "creation_date", updatable = false)
    private Date creationDate;

    @UpdateTimestamp
    @Column(name = "last_update")
    private Date lastUpdate;

    @Column(name = "state")
    private Integer state;

}
