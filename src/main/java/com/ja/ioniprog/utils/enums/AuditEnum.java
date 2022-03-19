package com.ja.ioniprog.utils.enums;

public enum AuditEnum {
    INSERT("INSERT"),
    UPDATE("UPDATE");

    final String name;

    AuditEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
