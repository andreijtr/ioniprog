package com.ja.ioniprog.model.dto.audit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ChangeDto {
    public String columnName;
    public String newValue;
    public String oldValue;

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
