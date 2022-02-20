package com.ja.ioniprog.model.entity.audit;

import com.ja.ioniprog.model.entity.Patient;
import com.ja.ioniprog.model.entity.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "patient_audit")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor @ToString
public class PatientAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_patient_audit")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_patient")
    private Patient patientEntity;

    @Embedded
    @AssociationOverride(name = "createdBy", joinColumns = @JoinColumn(name = "created_by"))
    @AttributeOverrides({
        @AttributeOverride(name = "createdOn",     column = @Column(name = "created_on")),
        @AttributeOverride(name = "actionType",    column = @Column(name = "action_type")),
        @AttributeOverride(name = "changes",       column = @Column(name = "changes")),
        @AttributeOverride(name = "entityVersion", column = @Column(name = "patient_version"))
    })
    private Audit audit;
}
