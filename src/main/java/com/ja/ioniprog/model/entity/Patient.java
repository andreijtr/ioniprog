package com.ja.ioniprog.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "patient")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor @ToString
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_patient")
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "birthday_date")
    private LocalDate birthdayDate;

    @Column(name = "details")
    private String details;

    @Column(name = "state")
    private String status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient)) return false;
        Patient patient = (Patient) o;
        return id == patient.id &&
                Objects.equals(firstName, patient.firstName) &&
                Objects.equals(lastName, patient.lastName) &&
                Objects.equals(phone, patient.phone) &&
                Objects.equals(birthdayDate, patient.birthdayDate) &&
                Objects.equals(details, patient.details) &&
                Objects.equals(status, patient.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, phone, birthdayDate, details, status);
    }
}
