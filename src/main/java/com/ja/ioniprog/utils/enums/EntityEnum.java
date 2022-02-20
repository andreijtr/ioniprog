package com.ja.ioniprog.utils.enums;

public enum EntityEnum {
    LOCATION ("LOCATION"),
    PATIENT ("PATIENT");

    final String name;

    EntityEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
