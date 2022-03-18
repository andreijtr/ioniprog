package com.ja.ioniprog.model.dto;

import java.util.Objects;

public class ChangeDto {
    public final String columnName;
    public final String newValue;
    public final String oldValue;

    public ChangeDto(String columnName, String newValue, String oldValue) {
        this.columnName = columnName;
        this.newValue = newValue;
        this.oldValue = oldValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChangeDto)) return false;
        ChangeDto changeDto = (ChangeDto) o;
        return Objects.equals(columnName, changeDto.columnName) &&
                Objects.equals(newValue, changeDto.newValue) &&
                Objects.equals(oldValue, changeDto.oldValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnName, newValue, oldValue);
    }
}
