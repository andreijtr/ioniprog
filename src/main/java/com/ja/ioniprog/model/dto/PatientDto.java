package com.ja.ioniprog.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class PatientDto {
    private String idPatient;
    private String firstName;
    private String lastName;
    private String phone;
    private String birthdayDate;
    private String details;
    private String status;
    //private UserShortDto doctorResponsible;
}
