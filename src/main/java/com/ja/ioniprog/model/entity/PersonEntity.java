package com.ja.ioniprog.model.entity;

import lombok.Data;

@Data
public class PersonEntity {
    private int id;

    private String firstName;

    private String lastName;

    private String phone;

    private String birthdayDate;

    private String nationalIdentifier;

    private String state;
}
