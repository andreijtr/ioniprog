package com.ja.ioniprog.exception.person;

public class PersonNotFoundException extends Exception {
    public PersonNotFoundException() {
        super("Person not found!");
    }
}
