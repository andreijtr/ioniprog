package com.ja.ioniprog.model.dto;

import lombok.*;

@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class UserShortDto {
    private String idUser;
    private String firstName;
    private String lastName;
    private String phone;
    private String username;
}
