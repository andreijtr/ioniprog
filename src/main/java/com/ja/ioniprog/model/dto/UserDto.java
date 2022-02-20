package com.ja.ioniprog.model.dto;

import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class UserDto implements Serializable {
    private String idUser;
    private String firstName;
    private String lastName;
    private String phone;

    private String username;
    private boolean expired;
    private String loginAttemptsRemaining;

    private Set<String> roles = new HashSet<>();
}
