package com.ja.ioniprog.model.dto;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString @Builder
public class UserDto {
    private String idUser;
    private String firstName;
    private String lastName;
    private String phone;

    private String username;
    private boolean expired;
    private String loginAttemptsRemaining;

    private Set<String> roles = new HashSet<>();
}
