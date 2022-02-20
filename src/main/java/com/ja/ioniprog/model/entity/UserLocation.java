package com.ja.ioniprog.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "USER_LOCATION")
@Data
public class UserLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user_location")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_location")
    private Location location;

    @Column(name = "assignment_date")
    private LocalDateTime assignmentDate;

    @Column(name = "retreat_date")
    private LocalDateTime retreatDate;

    @Column(name = "details")
    private String details;

    @Column(name = "state")
    private String state;
}
