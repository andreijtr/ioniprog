package com.ja.ioniprog.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

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

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "id_doctor_responsible")
//    private User doctorResponsible;

    @Column(name = "state")
    private String status;
}
