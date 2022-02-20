package com.ja.ioniprog.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "patient_doctor")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
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
}
