package com.ja.ioniprog.utils.enums;

public enum StateEnum {
    ACTIVE("ACTIVE"),
    DELETED("DELETED");

    String name;

    StateEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
