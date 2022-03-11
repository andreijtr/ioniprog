package com.ja.ioniprog.model.entity;

import com.ja.ioniprog.utils.enums.StateEnum;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "patient_doctor")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PatientDoctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_patient_doctor")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_patient")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_doctor")
    private User doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "deleted_on")
    private LocalDateTime deletedOn;

    @Column(name = "state")
    private String state;

    @Override
    public String toString() {
        return "PatientDoctor{" +
                "id=" + id +
                ", patient=" + patient +
                ", doctor=" + doctor +
                ", createdBy=" + createdBy +
                ", createdOn=" + createdOn +
                ", deletedOn=" + deletedOn +
                ", state='" + state + '\'' +
                '}';
    }

    public static PatientDoctor createPatientDoctor(Patient patient, User doctor, User createdBy) {
        PatientDoctor patientDoctor = new PatientDoctor();
        patientDoctor.setPatient(patient);
        patientDoctor.setDoctor(doctor);
        patientDoctor.setCreatedBy(createdBy);
        patientDoctor.setCreatedOn(LocalDateTime.now());
        patientDoctor.setState(StateEnum.ACTIVE.getName());
        return patientDoctor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PatientDoctor)) return false;
        PatientDoctor that = (PatientDoctor) o;
        return id == that.id &&
                Objects.equals(patient.getId(), that.patient.getId()) &&
                Objects.equals(doctor.getId(), that.doctor.getId()) &&
                Objects.equals(createdBy.getId(), that.createdBy.getId()) &&
                Objects.equals(createdOn, that.createdOn) &&
                Objects.equals(deletedOn, that.deletedOn) &&
                Objects.equals(state, that.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, patient.getId(), doctor.getId(), createdBy.getId(), createdOn, deletedOn, state);
    }
}
