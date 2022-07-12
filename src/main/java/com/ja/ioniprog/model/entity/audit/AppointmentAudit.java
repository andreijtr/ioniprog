package com.ja.ioniprog.model.entity.audit;

import com.ja.ioniprog.model.entity.Appointment;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "appointment_audit")
@Getter @Setter @AllArgsConstructor @ToString @NoArgsConstructor
public class AppointmentAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_appointment")
    private Appointment appointmentEntity;

    @Embedded
    @AssociationOverride(name = "createdBy", joinColumns = @JoinColumn(name = "created_by"))
    @AttributeOverrides({
            @AttributeOverride(name = "createdOn",     column = @Column(name = "created_on")),
            @AttributeOverride(name = "actionType",    column = @Column(name = "action_type")),
            @AttributeOverride(name = "changes",       column = @Column(name = "changes")),
            @AttributeOverride(name = "entityVersion", column = @Column(name = "appointment_version"))
    })
    private Audit audit;

    public AppointmentAudit(Appointment appointmentEntity, Audit audit) {
        this.appointmentEntity = appointmentEntity;
        this.audit = audit;
    }
}
