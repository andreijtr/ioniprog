package com.ja.ioniprog.utils.enums;

public enum RoleEnum {
    DOCTOR("DOCTOR"),
    ADMIN("ADMIN"),
    MANAGER("MANAGER");

    final String name;

    RoleEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
