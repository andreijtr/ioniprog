package com.ja.ioniprog.model.entity;

import lombok.*;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
@SelectBeforeUpdate
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "pwd")
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserRole> roles = new ArrayList<>();

    @Column(name = "expired")
    private int expired;

    @Column(name = "login_attempts_remaining")
    private int loginAttemptsRemaining;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone")
    private String phone;

//    public void addRole(Role role) {
//        UserRole userRole = new UserRole(this, role);
//        roles.add(userRole);
//    }

    //lated edit: faci un userRoleDao separat care sa gestioneze rolurile unui user

    //trebuie de vazut cum faci cu metodele astea, daca doar dezactivezi rolurile sau le stergi de tot.
    // daca doar le dezactivezi, iei constrangerea aia, dar ai grija din aplicatie sa nu poti adauga acelasi rol de 2 ori.

    // metodele astea nici nu ar trebui sa existe. faci un dao separat pt userRole si acolo adaugi, dezactivezi roluri. aici folosesti associations doar pt fetch-uri
//    public void removeRole(Role role) {
//        for (Iterator<UserRole> iterator = roles.iterator(); iterator.hasNext(); ) {
//            UserRole userRole = iterator.next();
//
//            if (userRole.getUser().equals(this) && userRole.getRole().equals(role)) {
//                iterator.remove();
//                userRole.setUser(null);
//                userRole.setRole(null);
//            }
//        }
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User that = (User) o;
        return id == that.id &&
                expired == that.expired &&
                loginAttemptsRemaining == that.loginAttemptsRemaining &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, expired, loginAttemptsRemaining);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", notExpired=" + expired +
                ", loginAttemptsRemaining=" + loginAttemptsRemaining +
                '}';
    }
}
