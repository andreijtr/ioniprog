package com.ja.ioniprog.utils.enums;

public enum ApplicationEnum {

    LOGGED_USER ("LOGGED_USER"),
    LOGIN_ATTEMPTS("5"),
    DATE_FORMATTER ("dd.MM.yyyy"),
    DATETIME_FORMATTER ("dd.MM.yyyy HH:mm:ss");

    private String name;

    ApplicationEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
