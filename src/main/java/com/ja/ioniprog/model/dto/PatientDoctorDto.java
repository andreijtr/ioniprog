package com.ja.ioniprog.model.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class PatientDoctorDto {
    private long idPatientDoctor;
    private PatientDto patientDto;
    private UserShortDto doctorDto;
    private UserShortDto createdBy;
    private String createdOn;
    //private UserShortDto deletedBy;
    private String deletedOn;
    private String state;
}
