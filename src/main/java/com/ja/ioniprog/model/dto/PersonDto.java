package com.ja.ioniprog.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
public class PersonDto {
    private String idPerson;
    private String firstName;
    private String lastName;
    private String phone;
    private String birthdayDate;
    private String nationalIdentifier;
    private String state;

    public PersonDto() {
    }

    public PersonDto(String idPerson, String firstName, String lastName, String phone, String birthdayDate, String nationalIdentifier, String state) {
        this.idPerson = idPerson;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.birthdayDate = birthdayDate;
        this.nationalIdentifier = nationalIdentifier;
        this.state = state;
    }
}
