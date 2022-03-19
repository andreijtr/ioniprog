package com.ja.ioniprog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Data @NoArgsConstructor @Builder @AllArgsConstructor
public class PatientDto {
    private String idPatient;
    @NotBlank(message = "Field cannot be empty.")
    private String firstName;
    @NotBlank(message = "Field cannot be empty.")
    private String lastName;
    @NotBlank(message = "Field cannot be empty.")
    private String phone;
    @NotBlank(message = "Field cannot be empty.")
    private String birthdayDate;
    private String details;
    private String status;
    private int version;

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
