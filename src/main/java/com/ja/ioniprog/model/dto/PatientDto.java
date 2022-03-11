package com.ja.ioniprog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data @NoArgsConstructor @Builder @AllArgsConstructor
public class PatientDto {
    private String idPatient;
    private String firstName;
    private String lastName;
    private String phone;
    private String birthdayDate;
    private String details;
    private String status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PatientDto)) return false;
        PatientDto that = (PatientDto) o;
        return Objects.equals(idPatient, that.idPatient) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(birthdayDate, that.birthdayDate) &&
                Objects.equals(details, that.details) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPatient, firstName, lastName, phone, birthdayDate, details, status);
    }
}
