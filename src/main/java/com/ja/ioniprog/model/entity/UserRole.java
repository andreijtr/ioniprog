package com.ja.ioniprog.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "user_role")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user_role")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_role")
    @JsonIgnore
    private Role role;

    @Column(name = "assignment_date")
    private String assignmentDate;

    @Column(name = "expiration_date")
    private String expirationDate;

    @Column(name = "non_expired")
    private int notExpired;

    public UserRole(User user, Role role) {
        this.user = user;
        this.role = role;
        this.assignmentDate = LocalDateTime.now().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRole)) return false;
        UserRole userRole = (UserRole) o;
        return notExpired == userRole.notExpired &&
                Objects.equals(id, userRole.id) &&
                Objects.equals(user, userRole.user) &&
                Objects.equals(role, userRole.role) &&
                Objects.equals(assignmentDate, userRole.assignmentDate) &&
                Objects.equals(expirationDate, userRole.expirationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, role, assignmentDate, expirationDate, notExpired);
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "id=" + id +
                ", user_id=" + user.getId() +
                ", role=" + role.getId() +
                ", assignmentDate='" + assignmentDate + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                ", notExpired=" + notExpired +
                '}';
    }
}
