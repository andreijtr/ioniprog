package com.ja.ioniprog.model.entity.audit;

import com.ja.ioniprog.model.entity.Patient;
import com.ja.ioniprog.model.entity.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "patient_audit")
@Getter @Setter @AllArgsConstructor @ToString @NoArgsConstructor
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

    public PatientAudit(Patient patient, Audit audit) {
        this.setPatientEntity(patient);
        this.setAudit(audit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PatientAudit)) return false;
        PatientAudit that = (PatientAudit) o;
        return id == that.id &&
                Objects.equals(patientEntity, that.patientEntity) &&
                Objects.equals(audit, that.audit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, patientEntity, audit);
    }
}
